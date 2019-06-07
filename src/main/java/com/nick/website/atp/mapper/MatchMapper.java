package com.nick.website.atp.mapper;

import com.nick.website.atp.pojo.H2HResult;
import com.nick.website.atp.pojo.MalePlayer;
import com.nick.website.atp.pojo.Match;
import com.nick.website.atp.pojo.Result;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Nick Yuan
 * @date 2019/5/25
 * @mood shitty
 */
public interface MatchMapper extends Mapper<Match> {
    @Select("select id from h2h where p1_id=#{id1} and " +
            "p2_id=#{id2} and tour_id=#{tour_id} and round=#{round}")
    Match check(@Param("id1") Integer id1, @Param("id2") Integer id2,
                @Param("tour_id") Integer tour_id, @Param("round") Integer round);

    @Select("select y2.winner,y2.score,y2.loser,tour.name tour_name,tour.time,\n" +
            "y2.round from \n" +
            "(select player1.name winner,y1.score,y1.name loser,y1.tour_id,\n" +
            "y1.round from \n" +
            "(select h2h.p1_id,h2h.score,player.name,h2h.tour_id,\n" +
            "h2h.round from h2h join mplayer player on h2h.p2_id\n" +
            "=PLAYER.id where PLAYER.name=#{name}) y1\n" +
            "join mplayer player1 on y1.p1_id=player1.id) y2\n" +
            "join tournament tour on y2.tour_id=tour.id having \n" +
            "tour_name=#{tour_name}\n" +
            "UNION\n" +
            "select x2.winner,x2.score,x2.loser,tour.`name` tour_name,\n" +
            "tour.time time,x2.round from \n" +
            "(select x1.winner,x1.score,p1.`name` loser,x1.tour_id,x1.round from\n" +
            "(select p.name winner,m.score score,m.p2_id loser_id,\n" +
            "m.tour_id,m.round from h2h m join mplayer p on\n" +
            "m.p1_id=p.id having p.`name`=#{name}) x1\n" +
            "join mplayer p1 where x1.loser_id=p1.id) x2\n" +
            "join tournament tour \n" +
            "where x2.tour_id=tour.id having tour_name=#{tour_name} " +
            "order by\n" +
            "time desc,round desc;")
    List<Result> searchHistory(@Param("name") String name,
                               @Param("tour_name") String tour_name);
     @Select("select z.winner,z.loser,z.score,z.round,tour.name tour_name,\n" +
             "tour.time time,tour.`level` level from\n" +
             "(select x1.winner,x1.score,x1.round,p2.name loser,x1.tour_id\n" +
             "from \n" +
             "(select p1.name winner,h1.score,h1.round,h1.p2_id\n" +
             "loser_id,h1.tour_id from h2h h1 JOIN mplayer\n" +
             "p1 on h1.p1_id=p1.id where \n" +
             "p1.name=#{name1}) x1\n" +
             "join mplayer p2 on x1.loser_id=p2.id WHERE\n" +
             "p2.name=#{name2} UNION \n" +
             "select xx1.winner,xx1.score,xx1.round,pp2.name loser,xx1.tour_id\n" +
             "from \n" +
             "(select pp1.name winner,hh1.score,hh1.round,hh1.p2_id\n" +
             "loser_id,hh1.tour_id from h2h hh1 JOIN mplayer\n" +
             "pp1 on hh1.p1_id=pp1.id where \n" +
             "pp1.name=#{name2}) xx1\n" +
             "join mplayer pp2 on xx1.loser_id=pp2.id WHERE\n" +
             "pp2.name=#{name1}) z\n" +
             "join tournament tour on z.tour_id=tour.id\n" +
             "order by time desc,round desc;")
    List<H2HResult> compare(@Param("name1")String name1,@Param("name2")String name2);
}
