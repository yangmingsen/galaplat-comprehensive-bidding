package com.galaplat.comprehensive.bidding;

import org.galaplat.baseplatform.file.plugin.FilePlugin;
import org.slf4j.MDC;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.scheduling.annotation.EnableAsync;

import com.galaplat.base.core.common.utils.InetAddressUtils;
import com.galaplat.baseplatform.permissions.PermissionPlugin;
import com.galaplat.baseplatform.serialnumber.plugin2.SerialnumberPlugin2;


@EnableEurekaClient
@SpringBootApplication
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class,
        ManagementWebSecurityAutoConfiguration.class})
@EnableHystrix
@EnableAsync
@EnableFeignClients
@org.springframework.context.annotation.Import({com.galaplat.base.core.springboot.CommonConfig.class, PermissionPlugin.class,
	SerialnumberPlugin2.class, FilePlugin.class})
@EnableMBeanExport(registration = org.springframework.jmx.support.RegistrationPolicy.IGNORE_EXISTING)
public class GalaplatComprehensiveBiddingApp implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(GalaplatComprehensiveBiddingApp.class, args);
    }

    public void run(String... args) throws Exception {
        //~ 设置日志获取ip值
        MDC.put("IP", InetAddressUtils.getHostAddress());
    }


}

