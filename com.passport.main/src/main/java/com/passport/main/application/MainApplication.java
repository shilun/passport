package com.passport.main.application;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import com.common.application.AbstractApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,MongoAutoConfiguration.class})
@EnableDubboConfiguration
@ComponentScan(basePackages = {"com.passport", "com.common.config"})
public class MainApplication extends AbstractApplication {
    private static Logger logger = LoggerFactory.getLogger("starting");

    public static void main(String[] args) {
        MainApplication instance = MainApplication.getInstance(MainApplication.class);
        SpringApplication app = new SpringApplication(MainApplication.class);
        app.addListeners(instance.buildListeners());
        app.run();
    }

    @Override
    protected void starting() {
        logger.info("main.starting----------------");
    }

    @Override
    protected void startFailed(Throwable throwable) {
        logger.error("main.error", throwable);
    }

    @Override
    protected void started() {
        logger.info("main.started");
    }
}