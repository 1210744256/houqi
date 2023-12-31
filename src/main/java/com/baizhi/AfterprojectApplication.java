package com.baizhi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.baizhi.mapper")
public class AfterprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(AfterprojectApplication.class, args);
    }

}
