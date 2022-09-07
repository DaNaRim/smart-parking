package com.explorers.smartparking.config.spring;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.explorers.smartparking"})
public class MvcConfig implements WebMvcConfigurer {

    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    public static final List<Locale> SUPPORTED_LOCALES = Arrays.asList(
            DEFAULT_LOCALE,
            Locale.forLanguageTag("ru"),
            Locale.forLanguageTag("uk")
    );

    public static final String[] RESOURCES = new String[]{
            "css",
            "js",
            "img",
            "icons"
    };

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        for (String resource : RESOURCES) {
            registry.addResourceHandler("/" + resource + "/**")
                    .addResourceLocations("classpath:/static/" + resource + "/");
        }
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/{lang}").setViewName("index");
        registry.addViewController("/forgotPassword").setViewName("forgotPassword");
        registry.addViewController("/parking").setViewName("parking");
        registry.addViewController("/{lang}/parking").setViewName("parking");
        registry.addViewController("/errors/badToken").setViewName("errors/badToken");
        registry.addViewController("/forbidden").setViewName("forbidden");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LocaleInterceptor());
    }

    @Bean
    public LocaleResolver localeResolver() {
        final SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(DEFAULT_LOCALE);
        return localeResolver;
    }

    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(
                "classpath:/messages",
                "classpath:/validation",
                "classpath:/pages"
        );
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
