package com.biit.form.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({ "com.biit.form.manager", "com.biit.usermanager" })
@EnableJpaRepositories(basePackages = "com.biit.usermanager")
@EntityScan(basePackages = "com.biit.usermanager")
public class FormManagerApplicationTest {

	public static void main(String[] args) {
		SpringApplication.run(FormManagerApplicationTest.class, args);
	}
}