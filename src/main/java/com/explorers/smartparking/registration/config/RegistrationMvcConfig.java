package com.explorers.smartparking.registration.config;

import com.explorers.smartparking.registration.web.validator.PasswordMatchesValidator;
import com.explorers.smartparking.registration.web.validator.ValidEmailValidator;
import com.explorers.smartparking.registration.web.validator.ValidPasswordValidator;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableJpaRepositories("com.explorers.smartparking.registration.persistence.dao")
@EntityScan("com.explorers.smartparking.registration.persistence.model")
public class RegistrationMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/registration").setViewName("registration");
        registry.addViewController("/parking").setViewName("parkingPage");
    }

    @Bean
    public ValidEmailValidator usernameValidator() {
        return new ValidEmailValidator();
    }

    @Bean
    public PasswordMatchesValidator passwordMatchesValidator() {
        return new PasswordMatchesValidator();
    }

    @Bean
    public ValidPasswordValidator validPasswordValidator() {
        return new ValidPasswordValidator();
    }
}
