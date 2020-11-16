package com.yunchuang.artco.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login.html");
        registry.addViewController("/logout").setViewName("login.html");
        registry.addViewController("/login.html").setViewName("login.html");
        registry.addViewController("/regist.html").setViewName("regist.html");
        registry.addViewController("/forgetPwd.html").setViewName("forgetPwd.html");
        registry.addViewController("/index.html").setViewName("index.html");
        registry.addViewController("/restadycourse").setViewName("restadycourse.html");
        registry.addViewController("/diverTest").setViewName("diverTest.html");

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

}
