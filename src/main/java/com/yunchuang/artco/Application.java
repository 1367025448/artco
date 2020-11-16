package com.yunchuang.artco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableCaching
@MapperScan(basePackages ="com.yunchuang.artco.dao")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
        System.out.println("xxxxxx");
    }
}
