package com.sirun.util;

import java.security.ProtectionDomain;

import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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
	
	public static void main(String[] args){
		SpringApplication.run(Application.class,args);
	}
	
	@Test
	public void test(){
		ProtectionDomain protectionDomain = ResolvableType.class.getProtectionDomain();
		System.out.println(protectionDomain);
	}
	
}
