package com.explorers.smartparking.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("secrets")
public final class SecurityProperties {

    private String databaseUrl;
    private String databaseUsername;
    private String databasePassword;

    private String mailUsername;
    private String mailPassword;

    private String rememberMeKey;

    public SecurityProperties() {
    }

    public String rememberMeKey() {
        return rememberMeKey;
    }
}
