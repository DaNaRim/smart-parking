package com.explorers.smartparking.config.security;

import com.explorers.smartparking.config.spring.MvcConfig;
import com.explorers.smartparking.user.persistence.model.RoleName;
import com.explorers.smartparking.user.web.failHandler.AuthenticationFailureHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(AuthenticationFailureHandler authenticationFailureHandler,
                          UserDetailsServiceImpl userDetailsService) {
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .mvcMatchers(
                        Arrays.stream(MvcConfig.RESOURCES)
                                .map(s -> "/" + s + "/**")
                                .toArray(String[]::new)
                ).permitAll()
                .mvcMatchers(
                        "/", "/{lang}",
                        "/login", "/{lang}/login",
                        "/registration", "/{lang}/registration",
                        "/registerUser",
                        "/registrationConfirm",
                        "/resendRegistrationToken",
                        "/forgotPassword",
                        "/sendPassResetToken",
                        "/resetPasswordPage",
                        "/updateForgotPassword",
                        "/updateForgottenPassword",
                        "/errors/badToken").permitAll()
                .mvcMatchers(
                        "/user/**",
                        "/parking",
                        "/park/**").hasRole(RoleName.USER.name())
                .mvcMatchers("/guard/**").hasRole(RoleName.GUARD.name())
                .mvcMatchers("/parkAdmin/**").hasRole(RoleName.ADMIN.name())
                .mvcMatchers("/superAdmin/**").hasRole(RoleName.SUPER_ADMIN.name())
                .anyRequest().authenticated()

                .and()
                .csrf()


//                .and()
//                .exceptionHandling()
//                .accessDeniedPage("/forbidden")

                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/parking", true)
                .failureHandler(authenticationFailureHandler)

                .and()
                .logout()
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID", "remember-me")

                .and()
                .rememberMe()
                .key("r*bQin&BcqR&^1DKTUGo")
                .rememberMeParameter("remember-me")
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                .userDetailsService(userDetailsService)
                .useSecureCookie(true);
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }

}
