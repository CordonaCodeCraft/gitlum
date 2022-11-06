package com.ciklum.gitlum;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class IntegrationTest {

  public static final String FIRST_EXISTING_USER = "Bisu-bg";
  public static final String FIRST_EXISTING_REPO = "mobile-bg";
  public static final Integer FIRST_EXISTING_REPO_ID = 247308472;
  public static final String NON_EXISTING_USER = "NonExistingUser";
  public static final String NO_AUTH_TOKEN = "";

  @Autowired WiremockProperties wiremockProperties;
  private WireMockServer wireMockServer;

  @BeforeEach
  void beforeEach() {
    wireMockServer = new WireMockServer();
    configureFor(wiremockProperties.getHost(), wiremockProperties.getPort());
    wireMockServer.start();
  }

  @AfterEach
  void tearDown() {
    wireMockServer.stop();
  }
}
