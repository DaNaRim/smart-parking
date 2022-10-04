package com.explorers.smartparking.config.security;

import com.explorers.smartparking.config.security.auth.AuthenticationFailureHandler;
import com.explorers.smartparking.config.security.auth.CustomAuthenticationProvider;
import com.explorers.smartparking.config.security.auth.CustomUserDetailsService;
import com.explorers.smartparking.user.persistence.model.RoleName;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.concurrent.TimeUnit;

import static com.explorers.smartparking.config.spring.MvcConfig.RESOURCES;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final CustomUserDetailsService userDetailsService;
    private final SecurityProperties securityProperties;

    public SecurityConfig(CustomAuthenticationProvider customAuthenticationProvider,
                          AuthenticationFailureHandler authenticationFailureHandler,
                          CustomUserDetailsService userDetailsService,
                          SecurityProperties securityProperties) {
        this.customAuthenticationProvider = customAuthenticationProvider;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.userDetailsService = userDetailsService;
        this.securityProperties = securityProperties;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { //skipcq: JAVA-W1042
        http
                .authorizeRequests((authz) -> authz
                        .mvcMatchers(
                                RESOURCES.stream()
                                        .map(resource -> "/" + resource + "/**")
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
                                "/errors/badToken"
                        ).permitAll()
                        .mvcMatchers(
                                "/user/**",
                                "/parking",
                                "/park/**"
                        ).hasRole(RoleName.USER.name())
                        .mvcMatchers("/guard/**").hasRole(RoleName.GUARD.name())
                        .mvcMatchers("/parkAdmin/**").hasRole(RoleName.ADMIN.name())
                        .mvcMatchers("/superAdmin/**").hasRole(RoleName.SUPER_ADMIN.name())
                        .anyRequest().authenticated()
                )
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
                        .loginPage("/login").permitAll()
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/parking", true)
                        .failureHandler(authenticationFailureHandler)
                )
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID", "remember-me")
                )
                .rememberMe(httpSecurityRememberMeConfigurer -> httpSecurityRememberMeConfigurer
                        .key(securityProperties.rememberMeKey())
                        .rememberMeParameter("remember-me")
                        .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                        .userDetailsService(userDetailsService)
                        .useSecureCookie(true)
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception { //skipcq: JAVA-W1042
        return http
                .getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(customAuthenticationProvider)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

}
