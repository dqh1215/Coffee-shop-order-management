package com.architecture_solution.coffeeshop_order_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CoffeeshopOrderManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoffeeshopOrderManagementApplication.class, args);
	}

}
