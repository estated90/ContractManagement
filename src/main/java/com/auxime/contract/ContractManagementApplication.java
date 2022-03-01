package com.auxime.contract;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;

/**
 * @author Nicolas
 *
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories
@OpenAPIDefinition
@EnableFeignClients
@EnableAsync
@EnableScheduling
public class ContractManagementApplication {

	/**
	 * @param args argument for the application launch
	 */
	public static void main(String[] args) {
		SpringApplication.run(ContractManagementApplication.class, args);
	}

}
