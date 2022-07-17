package com.bank.getway;

import java.util.Date;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean; 

@SpringBootApplication
@EnableEurekaClient
public class GetwayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GetwayserverApplication.class, args);
	}
	
	
	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
	    return builder.routes()
	        .route(p -> p
	            .path("/bankAPI/accounts/**")
	            .filters(f -> f.rewritePath("/bankAPI/accounts/(?<segment>.*)","/${segment}")
	            				.addResponseHeader("X-Response-Time",new Date().toString()))
	            .uri("lb://accounts")).
	        route(p -> p
		            .path("/bankAPI/loans/**")
		            .filters(f -> f.rewritePath("/bankAPI/loans/(?<segment>.*)","/${segment}")
		            		.addResponseHeader("X-Response-Time",new Date().toString()))
		            .uri("lb://LOANS")).
	        route(p -> p
		            .path("/bankAPI/cards/**")
		            .filters(f -> f.rewritePath("/bankAPI/cards/(?<segment>.*)","/${segment}")
		            		.addResponseHeader("X-Response-Time",new Date().toString()))
		            .uri("lb://CARDS")).build();
	}

}
