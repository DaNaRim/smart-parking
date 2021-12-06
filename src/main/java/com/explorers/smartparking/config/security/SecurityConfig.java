package com.explorers.smartparking.config.security;

import com.explorers.smartparking.user.persistence.model.RoleName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .mvcMatchers("/",
                        "/login",
                        "/registration",
                        "/registerUser",
                        "/registrationConfirm",
                        "/resendRegistrationToken",
                        "/resetPassword",
                        "/sendPassResetToken",
                        "/resetPasswordPage",
                        "/updateForgottenPassword").permitAll()
                .mvcMatchers("/user/**",
                        "/parking",
                        "/park/**").hasRole(RoleName.USER.name())
                .mvcMatchers("/parkAdmin/**").hasRole(RoleName.ADMIN.name())
                .mvcMatchers("/superAdmin/**").hasRole(RoleName.SUPER_ADMIN.name())
                .anyRequest().authenticated()

                .and()
                .csrf()
                .disable()//TODO enable

//                .and()
//                .exceptionHandling()
//                .accessDeniedPage("/error/403")

//                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/parking", true)

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
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(encoder());
        return provider;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }

}
