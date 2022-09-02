package com.explorers.smartparking.config.spring;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.explorers.smartparking"})
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/img/**").addResourceLocations("classpath:/static/img/");
        registry.addResourceHandler("/icons/**").addResourceLocations("classpath:/static/icons/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/registration").setViewName("registration");
        registry.addViewController("/forgetPassword").setViewName("forgetPassword");
        registry.addViewController("/updateFogotPassword").setViewName("updateFogotPassword");
        registry.addViewController("/parking").setViewName("parking");
        registry.addViewController("/forbidden").setViewName("forbidden");
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
//        localeChangeInterceptor.setParamName("lang");
//        localeChangeInterceptor.setIgnoreInvalidLocale(true);
//        registry.addInterceptor(localeChangeInterceptor);
//    }

//    @Bean
//    public LocaleResolver localeResolver() {
//        CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
//        cookieLocaleResolver.setDefaultLocale(Locale.ENGLISH);
//        return cookieLocaleResolver;
//    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
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
                "classpath:/messages/en",
                "classpath:/messages/ua",
                "classpath:/messages/ru"
        );
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
