package com.biit.form.manager;

/*-
 * #%L
 * Form Manager Server (Rest)
 * %%
 * Copyright (C) 2019 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
@EnableJpaRepositories({ "com.biit.usermanager.repository", "com.biit.form.manager.repository" })
@EntityScan({ "com.biit.usermanager.entity", "com.biit.form.manager.entity" })
@EnableSwagger2
public class FormManagerServer {
	private static final String SWAGGER_REST_LOCATION = "com.biit.form.manager.rest";

	public static void main(String[] args) {
		SpringApplication.run(FormManagerServer.class, args);
	}

	@Bean
	public Docket templateApi() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage(SWAGGER_REST_LOCATION)).paths(PathSelectors.any())
				.build().alternateTypeRules(AlternateTypeRules.newRule(Class.class, String.class));
	}
}
