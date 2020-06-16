package com.galaplat.comprehensive.bidding.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import com.galaplat.base.core.springboot.converts.WebMvcConfigurationSupportConfigurer;
import com.galaplat.baseplatform.permissions.interceptor.AppAccountInterceptor;
import com.galaplat.baseplatform.permissions.interceptor.LoginSecurityInterceptor;
import com.galaplat.baseplatform.permissions.interceptor.PermissionsInterceptor;

@Configuration
public class MyWebMvcConfigSupport extends WebMvcConfigurationSupportConfigurer {



   /* @Bean
    public PermissionsInterceptor getPermissionsInterceptor() {
        return new PermissionsInterceptor();
    }

    @Bean
    public AppAccountInterceptor getAppAccountInterceptor() {
        return new AppAccountInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LoginSecurityInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/api/**").excludePathPatterns("/mobile/**");

        registry.addInterceptor(getAppAccountInterceptor()).addPathPatterns("/api/**")
                //京东授权回调地址过滤
                .excludePathPatterns("/api/oauth/jd/authorization").excludePathPatterns("/mobile/**");

        registry.addInterceptor(getPermissionsInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/sync/**", "/autoh/**").excludePathPatterns("/api/**").excludePathPatterns("/mobile/**");

    }*/
}

