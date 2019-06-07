package com.nick.website.atp.pojo;

import lombok.Data;

import javax.persistence.Table;

/**
 * @author Nick Yuan
 * @date 2019/5/25
 * @mood shitty
 */
@Data
@Table(name = "h2h")
public class Match {
    private Integer id;
    private Integer p1_id;
    private Integer p2_id;
    private Integer tour_id;
    private Integer p1_seed;
    private Integer p2_seed;
    private Integer p1_rank;
    private Integer p2_rank;
    private Integer p1_place;
    private Integer p2_place;
    private Integer finish;
    private String score;
    private Integer round;
}
