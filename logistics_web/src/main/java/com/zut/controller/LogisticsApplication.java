package com.zut.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import util.IdWorker;

@SpringBootApplication //标明启动类
@ComponentScan("com.zut") // 1. 多模块项目需要扫描的包
@EnableJpaRepositories("com.zut.dao") // 2. Dao 层所在的包
@EntityScan("com.zut.po") // 3. Entity 所在的包
public class LogisticsApplication {
    //启动类----springboot 依靠启动类启动项目
    public static void main(String[] args) {
        SpringApplication.run(LogisticsApplication.class);
    }

    //相当于之前在配置文件中，把一个类配在spring的bean标签中，为什么写在这呢？因为没地儿可写
    //表示把IDWorker类配置在SpringBean中，其他类中可以直接使用该对象，spring负责对该对象进行初始化
    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }

}
