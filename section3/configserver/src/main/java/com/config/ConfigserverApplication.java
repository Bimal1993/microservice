package com.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ConfigserverApplication {
//http://localhost:8071/accounts/default  | dev | prod
	public static void main(String[] args) {
		SpringApplication.run(ConfigserverApplication.class, args);
	}

}
