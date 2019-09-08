package com.passport.web.application;

import com.common.application.AbstractApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.passport", "com.common.config"})
public class WebApplication extends AbstractApplication {
    private static Logger logger = LoggerFactory.getLogger("starting");

    public static void main(String[] args) {
        WebApplication instance = WebApplication.getInstance(WebApplication.class);
        SpringApplication app = new SpringApplication(WebApplication.class);
        app.addListeners(instance.buildListeners());
        app.run();
    }

    @Override
    protected void starting() {
        logger.info("web.starting----------------");
    }

    @Override
    protected void startFailed(Throwable throwable) {
        logger.error("web.error", throwable);
    }

    @Override
    protected void started() {
        logger.info("web.started");
    }

}