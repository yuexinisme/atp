package com.nick.website.atp.mapper;

import com.nick.website.atp.pojo.PeakAndWeeks;
import com.nick.website.atp.pojo.Song;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Nick Yuan
 * @date 2019/6/3
 * @mood shitty
 */

public interface SongMapper extends Mapper<Song> {
    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into song_1 (name,pos,singer,time) values(#{name},#{pos}," +
            "#{singer},#{time})")
    int insert(Song song);
    @Select("select * from song_1 where name=#{name} and singer=#{singer}" +
            " and time=#{time}")
    Song check(@Param("name") String name,@Param("singer") String singer,
               @Param("time") String time);
    @Delete("delete from song_1 where time=#{time}")
    void deleteByDate(@Param("time") String date);
    @Select("select min(pos) peak, count(singer) weeks from song_1 where name=#{name} " +
            "and singer=#{singer} group by name;")
    PeakAndWeeks getPeakAndWeeks(@Param("name")String name, @Param("singer")String singer);
    @Update("update song_1 set peak=#{peak}, weeks=#{weeks} where name=" +
            "#{name} and singer=#{singer}")
    void updateByIdentity(@Param("name") String name,@Param("singer") String singer,
                          @Param("peak")Integer peak,@Param("weeks")Integer weeks);
    @Update("update song_1 set last_pos=#{pos} where name=#{name} and singer" +
            "=#{singer} and time=#{time}")
    void updateNextWeek(@Param("name") String name,
                        @Param("singer") String singer,@Param("time") String time,
                        @Param("pos") Integer pos);
    @Select("select pos,name,singer,peak,weeks,last_pos from song_1 where time=#{week} order by pos;")
    List<Song> getWeek(@Param("week") String week);
}
