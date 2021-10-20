package com.tmser.selenium;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync(proxyTargetClass=true)
@ComponentScan("com.tmser")
public class SeleniumMain  {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(SeleniumMain.class);
        springApplication.setBannerMode(Banner.Mode.OFF);
        springApplication.setHeadless(false);
        ConfigurableApplicationContext context = springApplication.run(args);
    }
}
