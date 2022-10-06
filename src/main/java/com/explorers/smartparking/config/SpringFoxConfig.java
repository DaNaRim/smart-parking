package com.explorers.smartparking.config;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
public class SpringFoxConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public WebMvcEndpointHandlerMapping webMvcEndpointHandlerMapping(WebEndpointsSupplier webSupplier,
                                                                     ServletEndpointsSupplier servletSupplier,
                                                                     ControllerEndpointsSupplier controllerSupplier,
                                                                     EndpointMediaTypes endpointMediaTypes,
                                                                     CorsEndpointProperties corsProperties,
                                                                     WebEndpointProperties webProperties,
                                                                     Environment environment
    ) {
        String basePath = webProperties.getBasePath();
        Collection<ExposableWebEndpoint> webEndpoints = webSupplier.getEndpoints();

        List<ExposableEndpoint<?>> allEndpoints = new ArrayList<>();
        allEndpoints.addAll(webEndpoints);
        allEndpoints.addAll(servletSupplier.getEndpoints());
        allEndpoints.addAll(controllerSupplier.getEndpoints());

        boolean shouldRegisterLinksMapping = webProperties.getDiscovery().isEnabled()
                && (StringUtils.hasText(basePath)
                || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));

        return new WebMvcEndpointHandlerMapping(
                new EndpointMapping(basePath),
                webEndpoints,
                endpointMediaTypes,
                corsProperties.toCorsConfiguration(),
                new EndpointLinksResolver(allEndpoints, basePath),
                shouldRegisterLinksMapping);
    }
}
