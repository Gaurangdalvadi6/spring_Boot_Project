package com.scratch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ScratchProjectApplication {
	public static void main(String[] args) {
		
		ConfigurableApplicationContext context = SpringApplication.run(ScratchProjectApplication.class, args);
		MyFirstService myFirstService = context.getBean(MyFirstService.class);

		System.out.println(myFirstService.tellAStory());
	}

//	@Bean
//	public MyFirstClass myFirstClass() {
//		return new MyFirstClass();
//	};

}