package com.nick.website.atp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(value = "com.nick.website.atp.mapper")
public class AtpApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtpApplication.class, args);
    }

}
