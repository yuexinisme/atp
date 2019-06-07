package com.nick.website.atp.mapper;

import com.nick.website.atp.pojo.MalePlayer;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Set;

/**
 * @author Nick Yuan
 * @date 2019/5/25
 * @mood shitty
 */

public interface MalePlayerMapper extends Mapper<MalePlayer> {
    @Select("select name from mplayer")
    Set<String> getAllNames();
    @Select("select * from mplayer")
    List<MalePlayer> getAll();
    @Select("update mplayer set flag=#{flag} where id=#{id}")
    void updateFlag(@Param("flag")String flag,@Param("id")Long id);
    @Select("select id from mplayer where name=#{name}")
    Integer getIdByName(String name);
    @Select("select * from mplayer where name=#{name}")
    MalePlayer getAllByName(String name);
    @Select("select * from mplayer where name like '%-%'")
    List<MalePlayer> get();
    @Select("select * from mplayer\n" +
            "limit #{offset},#{rows}")
    List<MalePlayer> getAllInfo(@Param("offset") Integer offset,
                                @Param("rows")Integer rows);
    @Select("select * from mplayer where name like '%${name}%'")
    List<MalePlayer> search(@Param("name")String name);
}
