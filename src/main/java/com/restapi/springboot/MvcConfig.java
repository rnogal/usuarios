package com.restapi.springboot;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer  {

	 public void addViewControllers(ViewControllerRegistry registry) {
	        registry.addViewController("/").setViewName("usuarios");
	        registry.addViewController("/login").setViewName("login");
	        registry.addViewController("/usuarios").setViewName("usuarios");
	    }
}
