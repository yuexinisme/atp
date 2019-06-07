package com.nick.website.atp.pojo;

import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

import javax.persistence.Table;
import java.util.Date;

/**
 * @author Nick Yuan
 * @date 2019/6/3
 * @mood shitty
 */
@Data
@Table(name="song_1")
public class Song {
    private Long id;
    private String name;
    private String singer;
    private Integer pos;
    private Integer peak;
    private Integer weeks;
    private Integer last_pos;
    private String time;
}
