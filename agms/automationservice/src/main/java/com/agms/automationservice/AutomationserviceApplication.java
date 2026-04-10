package com.agms.automationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AutomationserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutomationserviceApplication.class, args);
	}

}
