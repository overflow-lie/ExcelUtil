package com.sirun.util;

import java.security.ProtectionDomain;

import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.ResolvableType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class Application {
	
	@RequestMapping(value="/")
	String home(){
		return "hello world";
	}
	/**
	 * Spring Boot运行方法
	 * 
	 * 1.
	 */
	public static void main(String[] args){
		SpringApplication.run(Application.class,args);
	}
	
	/**
	 * Spring Boot运行方法
	 * 
	 * 2. 关闭banner
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void startSpringBoot_2(){
		SpringApplication application = new SpringApplication(Application.class);
		application.setShowBanner(false);//关闭banner
		application.run();
	}
	
	/**
	 * Spring Boot运行方法
	 * 
	 * 3. 修改banner
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void startSpringBoot_3(){
		new SpringApplicationBuilder().showBanner(true).sources(Application.class).run();
	}
	
	@Test
	public void test(){
		ProtectionDomain protectionDomain = ResolvableType.class.getProtectionDomain();
		System.out.println(protectionDomain);
	}
	
}
