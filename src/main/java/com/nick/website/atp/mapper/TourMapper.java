package com.nick.website.atp.mapper;

import com.nick.website.atp.pojo.Tournament;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.Set;

/**
 * @author Nick Yuan
 * @date 2019/5/25
 * @mood shitty
 */
public interface TourMapper extends Mapper<Tournament> {
    @Select("select name from tournament")
    Set<String> getAllNames();
    @Select("select * from tournament where name=#{name} and time=#{date}")
    Tournament check(@Param("name") String name, @Param("date") String date);
    @Select("select id from tournament where name=#{name} and time=#{year}")
    Integer getId(@Param("name") String name,@Param("year") String year);
}
