package com.passport.main.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.passport", "com.common.config"})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class MainApplication {
    private static Logger logger = LoggerFactory.getLogger("starting");

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class);
    }
}