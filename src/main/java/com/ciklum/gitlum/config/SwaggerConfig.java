package com.ciklum.gitlum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

  public static final String TITLE = "Gitlum - Ciklum application for GitHub";
  public static final String DESCRIPTION =
      "Demo web application for presenting public non-forked Github repositories for existing Github users in a minimalistic way";
  public static final String VERSION = "1.0";
  public static final String TERMS_OF_SERVICE_URL = "https://www.ciklum.com/terms-of-use";
  public static final String LICENSE = "Apache License Version 2.0";
  public static final String LICENSE_URL = "https://www.apache.org/licenses/LICENSE-2.0";
  public static final String CONTACT_NAME = "Ciklum";
  public static final String CONTACT_URL = "https://www.ciklum.com/contact-us";
  public static final String CONTACT_EMAIL = "press@ciklum.com";

  @Bean
  public Docket api() {
    return new Docket(SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.any())
        .build()
        .pathMapping("/")
        .apiInfo(metaData());
  }

  private ApiInfo metaData() {
    final var contact = new Contact(CONTACT_NAME, CONTACT_URL, CONTACT_EMAIL);
    return new ApiInfoBuilder()
        .title(TITLE)
        .description(DESCRIPTION)
        .version(VERSION)
        .termsOfServiceUrl(TERMS_OF_SERVICE_URL)
        .license(LICENSE)
        .licenseUrl(LICENSE_URL)
        .contact(contact)
        .build();
  }
}
