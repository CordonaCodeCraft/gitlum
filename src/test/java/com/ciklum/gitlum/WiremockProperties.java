package com.ciklum.gitlum;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "gitlum.wiremock")
@Getter
@Setter
public class WiremockProperties {
  private String host;
  private Integer port;
}
