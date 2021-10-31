package com.explorers.smartparking.parking.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableJpaRepositories("com.explorers.smartparking.parking.persistence.dao")
@EntityScan("com.explorers.smartparking.parking.persistence.model")
public class ParkingMvcConfig implements WebMvcConfigurer {
}
