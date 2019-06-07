package com.nick.website.atp.pojo;

import lombok.Data;

/**
 * @author Nick Yuan
 * @date 2019/5/27
 * @mood shitty
 */
@Data
public class Result {
    private String winner;
    private String loser;
    private String score;
    private String tour_name;
    private String time;
    private Integer round;
}
