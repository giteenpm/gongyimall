package org.jcut.gmall.config;

import org.jcut.gmall.interceptors.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
/*
拦截器的配置类，项目启动spring自动加载
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    AuthInterceptor authInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(authInterceptor).addPathPatterns("/**").excludePathPatterns("/error");
        super.addInterceptors(registry);
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("Content-Type","X-Requested-With","accept,Origin","Access-Control-Request-Method","Access-Control-Request-Headers","token")
                .allowedMethods("*")
                .allowedOrigins("*")
                .allowCredentials(true);

    }

}
