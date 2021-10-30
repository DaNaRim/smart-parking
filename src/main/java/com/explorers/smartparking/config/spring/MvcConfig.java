package com.explorers.smartparking.config.spring;

import com.explorers.smartparking.web.validator.PasswordMatchesValidator;
import com.explorers.smartparking.web.validator.ValidEmailValidator;
import com.explorers.smartparking.web.validator.ValidPasswordValidator;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
@EnableWebMvc
@EnableJpaRepositories("com.explorers.smartparking.persistence.dao")
@EntityScan("com.explorers.smartparking.persistence.model")
@ComponentScan(basePackages = {"com.explorers.smartparking"})
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/registration").setViewName("registration");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        registry.addInterceptor(localeChangeInterceptor);
    }

//    @Bean
//    public LocaleResolver localeResolver() {
//        CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
//        cookieLocaleResolver.setDefaultLocale(Locale.ENGLISH);
//        return cookieLocaleResolver;
//    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(
                "classpath:/messages/messages_uk"
        );
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
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
