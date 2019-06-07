package com.nick.website.atp.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;

/**
 * @author Nick Yuan
 * @date 2019/5/25
 * @mood shitty
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="mplayer")
public class MalePlayer {
    private Long id;
    private String name;
    private String country;
    private String birthplace;
    private String residence;
    private String twi_url;
    private String ins_url;
    private String birthday;
    private Integer turned_pro;
    private String mug;
    private String name_mp3;
    private Integer weight_lbs;
    private Integer weight_kg;
    private String height_foot;
    private Integer height_cm;
    private String style;
    private String coach;
    private String bio_personal;
    private String bio_year;
    private String flag;
}
