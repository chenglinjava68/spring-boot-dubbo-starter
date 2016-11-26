/*
 * Copyright 2014-2018 the original author or authors..
 * Support: http://www.devutil.cn
 * License: http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package com.alibaba.dubbo.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.test.ImportAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.ComponentScan;
import com.alibaba.dubbo.demo.config.ProviderConfiguration;


/**
 * spring boot dubbo starter
 * 
 * @author 柒葉
 * @date 2016年11月26日
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.alibaba.dubbo.demo")
@ImportAutoConfiguration(ProviderConfiguration.class)
public class MainApplication implements EmbeddedServletContainerCustomizer {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(MainApplication.class, args);
	}

	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		container.setPort(8081);
	}

}
