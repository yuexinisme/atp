package com.nick.website.atp.controller;

import com.nick.website.atp.mapper.SongMapper;
import com.nick.website.atp.pojo.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Nick Yuan
 * @date 2019/6/7
 * @mood shitty
 */

@RestController
@CrossOrigin
public class SongController {
    @Autowired
    SongMapper songMapper;
    @GetMapping("/hot100/{week}")
    public List<Song> getWeek(@PathVariable String week){
       return songMapper.getWeek(week);
    }
}
