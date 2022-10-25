package com.ciklum.gitlum.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "gitlum.request")
@Getter
@Setter
public class RequestProperties {
    private String defaultPageNumber;
    private String defaultResultsPerPage;
    private String userParamKey;
    private String pageNumberParamKey;
    private String resultsPerPageParamValue;
    private String authorizationHeaderKey;
    private String tokenPrefix;
}