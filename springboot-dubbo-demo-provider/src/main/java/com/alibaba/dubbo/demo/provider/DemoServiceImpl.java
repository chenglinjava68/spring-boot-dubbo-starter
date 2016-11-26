/*
 * Copyright 2014-2018 the original author or authors..
 * Support: http://www.devutil.cn
 * License: http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package com.alibaba.dubbo.demo.provider;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.demo.DemoService;
import com.alibaba.dubbo.rpc.RpcContext;

/**
 * dubbo服务提供方
 * 
 * @author 柒葉
 * @date 2016年11月26日
 */
@Service
@Component
public class DemoServiceImpl implements DemoService,InitializingBean {

	@Override
	public String sayHello(String name) {
		System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] Hello " + name
				+ ", request from consumer: " + RpcContext.getContext().getRemoteAddress());
		return "Hello " + name + ", response form provider: " + RpcContext.getContext().getLocalAddress();
	}
  
	@Override
	public void afterPropertiesSet() throws Exception {
		 System.out.println("***********************************");
	}
}
