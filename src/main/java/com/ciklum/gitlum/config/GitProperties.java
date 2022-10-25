package com.ciklum.gitlum.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "gitlum.git")
@Getter
@Setter
public class GitProperties {
	private String baseUrl;
	private String repositoriesUri;
	private String branchesUri;
}