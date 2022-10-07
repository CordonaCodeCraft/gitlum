package com.ciklum.gitlum.domain.usecase;

import com.ciklum.gitlum.domain.model.git.Repo;

public class Utils {

	public static boolean isNotForkedRepository(final Repo repo) {
		return !repo.getFork();
	}

	public static boolean isForkedRepository(final Repo repo) {
		return repo.getFork();
	}
}