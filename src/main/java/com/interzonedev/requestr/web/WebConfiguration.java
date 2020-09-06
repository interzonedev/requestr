package com.interzonedev.requestr.web;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration("requestr.web.config")
public class WebConfiguration implements WebMvcConfigurer {

	/**
	 * Gets the {@link MessageSource} instance to be used across the web layer of the application.
	 *
	 * @return Returns a {@link ReloadableResourceBundleMessageSource} instance set to cache messages for the lifetime
	 * of the application.
	 */
	@Bean(name = {"messageSource", "springbootdemo.web.messageSource"})
	public MessageSource getMessageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setBasenames("classpath:messages/errors/errors");
		messageSource.setCacheSeconds(-1);
		return messageSource;
	}

	/**
	 * Register mappings of URLs to views that do not require a controller.
	 *
	 * @param registry The {@link ViewControllerRegistry} for the application.
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("home/home");
		registry.addViewController("/home").setViewName("home/home");
		registry.addViewController("/error").setViewName("error/error");
	}

}
