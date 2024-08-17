package com.naver.OnATrip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OnATripApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnATripApplication.class, args);
	}

}
