package com.ciklum.gitlum.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "gitlum.endpoints")
@Getter
@Setter
public class EndpointProperties {
    private String baseUrl;
    private String getRepositories;
}