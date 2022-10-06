package com.ciklum.gitlum.domain.usecase;

import com.ciklum.gitlum.annotations.UseCase;
import com.ciklum.gitlum.domain.model.git.Repo;

import java.util.Arrays;

@UseCase
public class RemoveForkRepositories {

	public Repo[] invoke(final Repo[] source) {
		return Arrays
				.stream(source)
				.filter(this::isForkedRepository)
				.toArray(Repo[]::new);
	}

	private boolean isForkedRepository(final Repo repo) {
		return !repo.getFork();
	}
}
