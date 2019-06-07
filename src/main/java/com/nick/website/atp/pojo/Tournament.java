package com.nick.website.atp.pojo;

import lombok.Data;

import javax.persistence.Table;
import java.sql.Struct;

/**
 * @author Nick Yuan
 * @date 2019/5/25
 * @mood shitty
 */
@Data
@Table(name = "tournament")
public class Tournament {
    private Integer id;
    private String name;
    private String location;
    private String time;
    private Integer year;
    private String level;
    private Integer number;
    private String surface;
    private Integer prize;
    private Integer winner_id;
    private String image;
}
