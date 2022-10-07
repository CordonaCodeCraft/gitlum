package com.ciklum.gitlum;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class IntegrationTest {

	public static final String EXISTING_USER = "bisu-bg";
	public static final String EXISTING_REPO = "mobile-bg";
	public static final String NON_EXISTING_USER = "NonExistingUser";
	public static final String NON_EXISTING_REPO = "NonExistingRepo";
}