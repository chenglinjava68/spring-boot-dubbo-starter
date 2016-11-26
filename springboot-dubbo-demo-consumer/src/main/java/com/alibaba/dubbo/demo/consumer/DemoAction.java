/*
 * Copyright 2014-2018 the original author or authors..
 * Support: http://www.devutil.cn
 * License: http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package com.alibaba.dubbo.demo.consumer;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.demo.DemoService;

/**
 * dubbo consumer
 * 
 * @author 柒葉
 * @date 2016年11月26日
 */
@Component
public class DemoAction implements InitializingBean {
	@Reference
	private DemoService demoService;

	@Override
	public void afterPropertiesSet() throws Exception {
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			try {
				String hello = demoService.sayHello("world" + i);
				System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] " + hello);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Thread.sleep(2000);
		}
	}

}
