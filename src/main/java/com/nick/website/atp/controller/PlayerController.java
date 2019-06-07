package com.nick.website.atp.controller;

import com.nick.website.atp.mapper.MalePlayerMapper;
import com.nick.website.atp.mapper.MatchMapper;
import com.nick.website.atp.pojo.H2HResult;
import com.nick.website.atp.pojo.MalePlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Nick Yuan
 * @date 2019/5/27
 * @mood shitty
 */
@RestController
@CrossOrigin
public class PlayerController {
    @Autowired
    MalePlayerMapper malePlayerMapper;
    @Autowired
    MatchMapper matchMapper;
    @GetMapping("info")
    public List<MalePlayer> getAllInfo(@RequestParam Integer page, @RequestParam Integer rows){
        return malePlayerMapper.getAllInfo((page-1)*rows,rows);
    }
    @GetMapping("search")
    public List<MalePlayer> search(@RequestParam String name){
        return malePlayerMapper.search(name);
    }
    @GetMapping("h2h")
    public List<H2HResult> compare(@RequestParam String name1,
                                   @RequestParam String name2){
        return matchMapper.compare(name1,name2);
    }
    @GetMapping("name")
    public MalePlayer getByName(@RequestParam String name){
        return malePlayerMapper.getAllByName(name);
    }
}
