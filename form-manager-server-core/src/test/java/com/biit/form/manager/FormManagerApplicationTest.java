package com.biit.form.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"com.biit.form.manager", "com.biit.usermanager"})
@EnableJpaRepositories({"com.biit.usermanager.repository", "com.biit.form.manager.repository"})
@EntityScan({"com.biit.usermanager.entity", "com.biit.form.manager.entity"})
public class FormManagerApplicationTest {

    public static void main(String[] args) {
        SpringApplication.run(FormManagerApplicationTest.class, args);
    }
}