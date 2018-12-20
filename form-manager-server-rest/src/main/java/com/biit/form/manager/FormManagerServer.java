package com.biit.form.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan({ "com.biit.form.manager", "com.biit.usermanager" })
@EnableJpaRepositories(basePackages = "com.biit.usermanager")
@EntityScan(basePackages = "com.biit.usermanager")
@EnableSwagger2
public class FormManagerServer {
	private final static String SWAGGER_REST_LOCATION = "com.biit.form.manager.rest";

	public static void main(String[] args) {
		SpringApplication.run(FormManagerServer.class, args);
	}

	@Bean
	public Docket templateApi() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage(SWAGGER_REST_LOCATION)).paths(PathSelectors.any())
				.build().alternateTypeRules(AlternateTypeRules.newRule(Class.class, String.class));
	}
}
