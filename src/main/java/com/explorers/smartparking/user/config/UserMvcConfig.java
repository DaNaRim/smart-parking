package com.explorers.smartparking.user.config;

import com.explorers.smartparking.user.web.validator.PasswordMatchesValidator;
import com.explorers.smartparking.user.web.validator.ValidEmailValidator;
import com.explorers.smartparking.user.web.validator.ValidPasswordValidator;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableJpaRepositories("com.explorers.smartparking.user.persistence.dao")
@EntityScan("com.explorers.smartparking.user.persistence.model")
public class UserMvcConfig implements WebMvcConfigurer {

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
