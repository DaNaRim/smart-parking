package com.explorers.smartparking.config.spring;

import com.explorers.smartparking.config.security.SecurityProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class MvcConfig implements WebMvcConfigurer {

    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    public static final List<Locale> SUPPORTED_LOCALES = Arrays.asList(
            DEFAULT_LOCALE,
            Locale.forLanguageTag("ru"),
            Locale.forLanguageTag("uk")
    );

    public static final List<String> RESOURCES = List.of(
            "css",
            "js",
            "img",
            "icons"
    );

    private static final List<String> SQL_INIT_SCRIPTS = List.of(
            "data-roles.sql"
    );

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
        registry.addViewController("/forgotPassword").setViewName("forgotPassword");
        registry.addViewController("/errors/badToken").setViewName("errors/badToken");
        registry.addViewController("/forbidden").setViewName("forbidden");

        for (Locale locale : SUPPORTED_LOCALES) {
            registry.addViewController("/" + locale.getLanguage()).setViewName("index");
            registry.addViewController("/" + locale.getLanguage() + "/forgotPassword").setViewName("forgotPassword");
        }
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

    @Bean
    public DataSourceInitializer dataSourceInitializer(@Qualifier("dataSource") final DataSource dataSource) {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();

        for (String script : SQL_INIT_SCRIPTS) {
            resourceDatabasePopulator.addScript(new ClassPathResource("/sql/" + script));
        }

        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(dataSource);
        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
        return dataSourceInitializer;
    }

}
