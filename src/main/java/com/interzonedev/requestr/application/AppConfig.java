package com.interzonedev.requestr.application;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({"classpath*:com/interzonedev/requestr/applicationContext-requestr.xml"})
@ComponentScan(basePackages = {"com.interzonedev.requestr.web"})
public class AppConfig {
}
