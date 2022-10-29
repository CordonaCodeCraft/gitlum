package com.ciklum.gitlum;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class IntegrationTest {
  public static final String FIRST_EXISTING_USER = "Bisu-bg";
  public static final String SECOND_EXISTING_USER = "CordonaCodeCraft";
  public static final String FIRST_EXISTING_REPO = "mobile-bg";
  public static final Integer FIRST_EXISTING_REPO_ID = 247308472;
  public static final String SECOND_EXISTING_REPO = "amigoscode-microservices-demo";
  public static final String NON_EXISTING_USER = "NonExistingUser";
  public static final String TOKEN = "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";
}
