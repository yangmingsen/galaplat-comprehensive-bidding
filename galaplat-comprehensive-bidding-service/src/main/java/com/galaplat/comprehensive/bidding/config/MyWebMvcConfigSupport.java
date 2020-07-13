package com.galaplat.comprehensive.bidding.config;


import com.galaplat.baseplatform.permissions.interceptor.DatabaseSchemaInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import com.galaplat.base.core.springboot.converts.WebMvcConfigurationSupportConfigurer;
import com.galaplat.baseplatform.permissions.interceptor.AppAccountInterceptor;
import com.galaplat.baseplatform.permissions.interceptor.LoginSecurityInterceptor;
import com.galaplat.baseplatform.permissions.interceptor.PermissionsInterceptor;

@Configuration
public class MyWebMvcConfigSupport extends WebMvcConfigurationSupportConfigurer {


    @Bean
    public PermissionsInterceptor getPermissionsInterceptor() {
        return new PermissionsInterceptor();
    }

    @Bean
    public AppAccountInterceptor getAppAccountInterceptor() {
        return new AppAccountInterceptor();
    }

    @Bean
    public DatabaseSchemaInterceptor getDatabaseSchemaInterceptor() {
        return new DatabaseSchemaInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginSecurityInterceptor()).addPathPatterns("/**").excludePathPatterns("/api/**");
        registry.addInterceptor(getDatabaseSchemaInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(getAppAccountInterceptor()).addPathPatterns("/api/**");
        super.addInterceptors(registry);
    }


/*   @Bean
    public MyInterceptor getMyInterceptor() {
       return new MyInterceptor();
   }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getMyInterceptor())
                .addPathPatterns("/**") //必须使用 /** 才能拦截所有
                .excludePathPatterns("/jbxt/user/login");
    }*/


}

