package com.ciklum.gitlum.domain.usecase;

import com.ciklum.gitlum.annotations.UseCase;
import com.ciklum.gitlum.domain.model.dto.BranchDTO;
import com.ciklum.gitlum.domain.model.dto.RepoDTO;
import com.ciklum.gitlum.domain.model.git.Repo;
import com.ciklum.gitlum.domain.model.mapping.BranchMapper;
import com.ciklum.gitlum.domain.model.mapping.RepoMapper;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UseCase
@RequiredArgsConstructor
public class ProduceResult {

	private final GetRepositories getRepositories;
	private final GetBranches getBranches;

	public List<RepoDTO> invoke(final String userName) {
		return Arrays
				.stream(getRepositories.invoke(userName))
				.map(this::toDTO)
				.toList();
	}

	private RepoDTO toDTO(final Repo source) {
		final var dto = RepoMapper.INSTANCE.entityToDTO(source);
		dto.setBranches(initializeBranches(dto));
		return dto;
	}

	private Set<BranchDTO> initializeBranches(final RepoDTO source) {
		return Arrays
				.stream(getBranches.invoke(source.getOwnerLogin(), source.getRepositoryName()))
				.map(BranchMapper.INSTANCE::entityToDTO)
				.collect(Collectors.toSet());
	}

}
