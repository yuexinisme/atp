package com.nick.website.atp.pojo;

import lombok.Data;

/**
 * @author Nick Yuan
 * @date 2019/5/27
 * @mood shitty
 */
@Data
public class H2HResult {
    private String score;
    private Integer round;
    private String tour_name;
    private String time;
    private String level;
    private String winner;
    private String loser;
}
