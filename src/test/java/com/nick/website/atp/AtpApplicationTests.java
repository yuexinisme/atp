package com.nick.website.atp;

import com.nick.website.atp.mapper.MalePlayerMapper;
import com.nick.website.atp.mapper.MatchMapper;
import com.nick.website.atp.mapper.SongMapper;
import com.nick.website.atp.mapper.TourMapper;
import com.nick.website.atp.pojo.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AtpApplicationTests {
@Autowired
    MalePlayerMapper malePlayerMapper;
@Autowired
    TourMapper tourMapper;
@Autowired
     MatchMapper matchmapper;
@Autowired
    SongMapper songMapper;
List<Integer>months;
HashSet<String>dates;
void pre(){
    months=new ArrayList<Integer>();
    months.add(1);
    months.add(3);
    months.add(5);
    months.add(7);
    months.add(8);
    months.add(10);
    months.add(12);
}
    @Test
    public void contextLoad(){
        Set<String> names = malePlayerMapper.getAllNames();
        Document document = null;
        for(int year=2019;year>1999;year--){
            try {
                document = Jsoup.connect("https://www.atptour.com/en/rankings/singles?rankDate="+String.valueOf(year)+"-05-20&rankRange=1-100")
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements elements = document.select("a[href~=/en/players/.*/overview]");
            for(Element e:elements){
                if(names.contains(e.attr("data-ga-label")))continue;
                loadMPlayer(e.attr("data-ga-label"),"https://www.atptour.com"+e.attr("href"));
            }
        }


    }
    public boolean loadMPlayer(String playerName, String url){
        MalePlayer player=new MalePlayer();
        System.out.println(playerName);
        player.setName(playerName);
        Document document = null;
        try {
            document = Jsoup.connect(url)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        String country = document.select("div[class=player-flag-code]").text();
        player.setCountry(country);
        String twi_url = document.select("a[href~=https://twitter.com/.*]").attr("data-ga-label");
        player.setTwi_url(twi_url);
        String ins_url = document.select("a[href~=https://www.instagram.com/.*]").attr("data-ga-label");
        player.setIns_url(ins_url);
        String birthday = document.select("span[class=table-birthday]").text();
        player.setBirthday(birthday);
        Elements elements = document.select("div[class=table-big-value]");
        for(Element e:elements){
            if(e.text().length()==4){
                player.setTurned_pro(Integer.valueOf(e.text()));
                break;
            }
        }
        String src = document.select("img[src~=/-/media/tennis/players/.*]").attr("src");
        player.setMug("https://www.atptour.com"+src);
        String mp3 = document.select("source[src~=/-/media/player-names/.*]").attr("src");
        player.setName_mp3("https://www.atptour.com"+mp3);
        String weight_lbs=document.select("span[class=table-weight-lbs]").text();
        if(!weight_lbs.equals(""))
        player.setWeight_lbs(Integer.valueOf(weight_lbs));
        String weight_kg=document.select("span[class=table-weight-kg-wrapper]").text();
        if(!weight_kg.equals(""))
        player.setWeight_kg(Integer.valueOf(weight_kg.substring(1,weight_kg.length()-3)));
        String height_foot=document.select("span[class=table-height-ft]").text();
        if(height_foot!=""&&height_foot!=null)
        player.setHeight_foot(height_foot);
        String height_cm = document.select("span[class=table-height-cm-wrapper]").text();
        if(height_cm.length()!=0&&height_cm!=null)
        player.setHeight_cm(Integer.valueOf(height_cm.substring(1,height_cm.length()-3)));
        Elements eles = document.select("div[class=table-value]");
        int index=0;
        for(Element e:eles){
            switch (index){
                case 0:
                    player.setBirthplace(e.text().trim());
                    index++;
                    break;
                case 1:
                    player.setResidence(e.text().trim());
                    index++;
                    break;
                case 2:
                    player.setStyle(e.text().trim());
                    index++;
                    break;
                case 3:
                    player.setCoach(e.text().trim());
            }
        }
        malePlayerMapper.insert(player);
        System.out.println(playerName+" saved!");
        return true;
    }

    public static void main(String[] args) throws Exception{
        new AtpApplicationTests().loadMPlayer("Novak","https://www.atptour.com/en/players/novak-djokovic/d643/overview");
    }
    @Test
    public void loadTour() {
        Tournament tour=new Tournament();
        for(int year=2014;year>2013;year--){
            Document document = null;
            try {
                document = Jsoup.connect("https://www.atptour.com/en/scores/results-archive?year=" + year)
                        .get();
            } catch (IOException e) {
               e.printStackTrace();

            }
            Elements elements = document.select("tr[class=tourney-result]");
            for(Element e:elements){
                String name = e.select("span[class=tourney-title]").text();

                    Tournament t = tourMapper.check(name, e.select("span[class=tourney-dates]").text().trim());
                    if(t!=null)continue;
                tour.setName(name);
                String location=e.select("span[class=tourney-location]").text().trim();
                tour.setLocation(location);
                String date=e.select("span[class=tourney-dates]").text().trim();
                tour.setTime(date);
               // System.out.println(tour);
                tour.setYear(Integer.valueOf(date.split("\\.")[0]));
                String number=e.select("a[href~=.*matchtype=singles]").text().trim();
                if(!number.equals(""))
                tour.setNumber(Integer.valueOf(number));
                Elements eles = e.select("td[class=tourney-details]");
                for(Element el:eles){
                    if(!el.text().startsWith("Results") && !el.text().startsWith("SGL")){
                        tour.setSurface(el.text().replaceAll("\\s+"," "));
                    }
                }
                String prize = e.select("td[class=tourney-details fin-commit]").text();
                if(!prize.equals(""))
                tour.setPrize(Integer.valueOf(prize.replaceAll("\\$|,|€|A|£","")));
                Elements es = e.select("div[class=tourney-detail-winner]");
                for(Element e1:es){
                    if(e1.text().startsWith("SGL")){
                        System.out.println(e1.text());
                        if(e1.text().split(":").length!=1) {
                            String winnerName = e1.text().split(":")[1].trim();
                            Integer id = malePlayerMapper.getIdByName(winnerName);
                            tour.setWinner_id(id);
                        }
                    }
                }
                String url = e.select("img[alt=tournament badge]").attr("src");
                if(url.contains("250")){
                    tour.setLevel("250");
                }else if(url.contains("500")){
                    tour.setLevel("500");
                }else if(url.contains("1000")){
                    tour.setLevel("1000");
                }else if(url.contains("slam")){
                    tour.setLevel("slam");
                }
                tourMapper.insert(tour);
                System.out.println(tour);
            }
        }

    }

    public void loadMatch(String path) throws Exception{
        System.out.println(path);
        Document document = Jsoup.connect(path)
                .get();
        if(!document.select("h3[class=not-found-404]").text().equalsIgnoreCase(""))
            return;
        String tour_name = document.select("a[class=tourney-title]").text();
        String year="";
        if(document.select("span[class=tourney-dates]").text().trim().equalsIgnoreCase(""))
            year=path.split("/")[8];
        else
        year = document.select("span[class=tourney-dates]").text().trim().substring(0, 4);
        Integer tour_id=null;
        if(document.select("span[class=tourney-dates]").text().equalsIgnoreCase(""))
            return;
        else
       tour_id=tourMapper.getId(tour_name,document.select("span[class=tourney-dates]").text().trim().substring(0,10));
        Elements elements = document.select("td[rowspan~=.+]");
        for(Element e:elements){
            Match match=new Match();
            String num = e.attr("rowspan");
            match.setTour_id(tour_id);
            String url = e.select("a[href~=/en/players/fedex-head-2-head/.+]").attr("href");
            if(url.equals(""))continue;
            String[] names = url.split("/")[4].split("\\-vs\\-");
            String name1 = names[0].replaceAll("\\-", " ").trim();
            if(name1.equalsIgnoreCase("Christian Garin"))
                name1="Cristian Garin";
            Integer id1 =ran(name1);
            String name2 = names[1].replaceAll("\\-", " ").trim();
            if(name2.equalsIgnoreCase("Christian Garin"))
                name2="Cristian Garin";
            Integer id2 = ran(name2);
            Match m = matchmapper.check(id1, id2, tour_id, Integer.valueOf(num));
            if(m!=null)continue;
            match.setP1_id(id1);
            match.setP2_id(id2);
            String score = e.select("a[data-override-transition]").text().trim();
            match.setScore(score);
            match.setFinish(score.contains("RET")?0:1);

            match.setRound(Integer.valueOf(num));
            System.out.println(match);
            matchmapper.insert(match);
            System.out.println(match);
        }
    }
    @Test
    public void loadAllMatches() throws Exception{
        for(int year=2007;year>1999;year--) {
            Document document = null;
            try {
                document = Jsoup.connect("https://www.atptour.com/en/scores/results-archive?year=" + year)
                        .get();
            } catch (IOException e) {
                e.printStackTrace();

            }
            Elements elements = document.select("a[href~=/en/scores/.*/draws\\?matchtype=singles]");
            for(Element e:elements){
                loadMatch("https://www.atptour.com"+e.attr("href"));
            }

        }
    }

    public Integer ran(String name) throws Exception{
        if(name.equalsIgnoreCase("albert ramos violas"))
            name="Albert Ramos Vinolas";
        Integer id=malePlayerMapper.getIdByName(name);
        if(id!=null)return id;
        String[]parts=name.split(" ");
        StringBuilder builder=new StringBuilder();
        for(String part:parts){
            builder.append(part+"+");
        }
        Document document = Jsoup.connect("https://www.google.com/search?q=" + builder.toString()
                 + "atp").get();
        String url = document.select("a[href~=https://www.atptour.com/en/players/.*]")
                .attr("href");
        loadMPlayer(name,
               url);
        return malePlayerMapper.getIdByName(name);
//        List<MalePlayer> players = malePlayerMapper.get();
//        for(MalePlayer p:players){
//            String name=p.getName();
//            String new_name=name.replaceAll("\\-"," ");
//            p.setName(new_name);
//            malePlayerMapper.updateByPrimaryKey(p);
//            System.out.println(p);

    }
    @Test
    public void search(){
        List<Result> results = matchmapper.searchHistory("novak djokovic", "roland garros");
        for(Result r:results)
            System.out.println(r);
    }
    @Test
    public void change(){
        List<MalePlayer> players = malePlayerMapper.getAll();
        for(MalePlayer p:players){
            String flag="https://www.atptour.com/-/media/images/flags/"+p.getCountry().toLowerCase()+".svg";
            malePlayerMapper.updateFlag(flag,p.getId());
        }
    }
    public void addRankings(){
        String url="https://www.atptour.com/en/rankings/singles?rankDate=2019-05-20&rankRange=1-5000";

    }

    public void readMusicPos(String url) throws Exception{
     //String url="https://www.billboard.com/charts/hot-100/2002-01-01";
     List<Song>songs=new ArrayList<>();
        Document document = Jsoup.connect(url).get();
        Elements els1 = document.select("span[class=chart-list-item__title-text]");
        for(Element e1:els1){

            Song song=new Song();
            song.setName(e1.text());
            songs.add(song);
        }
        Elements els2 = document.select("div[class=chart-list-item__artist]");
        SimpleDateFormat format=new SimpleDateFormat("yyyy-mm-dd");
        Date d = format.parse("2002-01-01");
        for(int i=0;i<els2.size();i++){
            Song song = songs.get(i);
          //  System.out.println(els2.text());
            song.setSinger(els2.get(i).text());
            song.setPos(i+1);

            song.setTime(url.substring(41));
            Song s = songMapper.check(song.getName(), song.getSinger(), song.getTime());
            if(s!=null)continue;
            songMapper.insert(song);
            System.out.println("～～"+song);
        }

    }
    public String addDate(String date){

        Integer year=Integer.valueOf(date.substring(0,4));
        Integer month=Integer.valueOf(date.substring(5,7));
        Integer day=Integer.valueOf(date.substring(8));
        day+=7;
        int cutoff;
        if(months.contains(month)){
            cutoff=31;
        }else if(month!=2){
            cutoff=30;
        }else if(year % 4 == 0){
            cutoff=29;
        }else cutoff=28;
        if(day>cutoff) {
            day-=cutoff;
            month++;
        }
        if(month>12){
            month-=12;
            year++;
        }
        StringBuilder builder=new StringBuilder();
        builder.append(year+"-"+(month<10?("0"+month):month)+"-"+
                (day<10?("0"+day):day));
        return builder.toString();
    }
    @Test
    public void readBillboard() throws Exception{
        pre();
        String url="https://www.billboard.com/charts/hot-100/";
        dates=new HashSet<>();
        String date="1990-01-06";
        while(true){
            readMusicPos(url+date);
            date=addDate(date);
            if(date.equalsIgnoreCase("1991-01-05"))return;
        }
    }
    @Test
    public void removeDup() {
        pre();
        dates=new HashSet<>();
        String date="2000-01-01";
        while(!date.equalsIgnoreCase("2019-06-15")){
            date=addDate(date);
            dates.add(date);
        }
        List<Song>songs=songMapper.selectAll();
        for(Song s:songs){
            if(!dates.contains(s.getTime())){
                songMapper.deleteByDate(s.getTime());
            }
        }
    }
    @Test
    public void addPeakAndWeeks(){
        List<Song> songs = songMapper.selectAll();
        for(Song s:songs){
            if(s.getPeak()!=null)continue;
            PeakAndWeeks peakAndWeeks = songMapper.getPeakAndWeeks(s.getName(), s.getSinger());
            s.setPeak(peakAndWeeks.getPeak());
            s.setWeeks(peakAndWeeks.getWeeks());
            songMapper.updateByIdentity(s.getName(),s.getSinger(),
                    s.getPeak(),s.getWeeks());
        }
    }
    @Test
    public void addLastWeek(){
    pre();
        List<Song> songs = songMapper.selectAll();
        for(Song s:songs) {
            String time = s.getTime();
            String next = addDate(time);
            try {
                songMapper.updateNextWeek(s.getName(), s.getSinger(), next, s.getPos());
            }catch (Exception e){

            }
        }
    }
}
