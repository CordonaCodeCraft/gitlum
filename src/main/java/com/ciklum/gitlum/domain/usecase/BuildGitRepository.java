package com.ciklum.gitlum.domain.usecase;

import com.ciklum.gitlum.annotations.UseCase;
import com.ciklum.gitlum.domain.model.dto.BranchDTO;
import com.ciklum.gitlum.domain.model.dto.RepoDTO;
import com.ciklum.gitlum.domain.model.git.Branch;
import com.ciklum.gitlum.domain.model.git.Repo;
import com.ciklum.gitlum.domain.model.mappers.BranchMapper;
import com.ciklum.gitlum.domain.model.mappers.RepoMapper;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.Set;
import java.util.stream.Collectors;

@UseCase
@RequiredArgsConstructor
public class BuildGitRepository {

	private final GetRepositories getRepositories;
	private final GetBranches getBranches;
	private final RepoMapper repoMapper;
	private final BranchMapper branchMapper;

	public Flux<RepoDTO> invoke(final String userName) {
		return getRepositories
				.invoke(userName)
				.flatMap(this::repoToDTO);
	}

	private Mono<RepoDTO> repoToDTO(final Repo source) {
		final var dto = Mono.just(repoMapper.toDTO(source));
		final var branches = branchesToCollection(getBranches.invoke(source));
		return dto
				.zipWith(branches)
				.map(BuildGitRepository::asFinalProduct);
	}

	private Mono<Set<BranchDTO>> branchesToCollection(final Flux<Branch> source) {
		return source
				.map(branchMapper::ToDTO)
				.collect(Collectors.toSet());
	}

	private static RepoDTO asFinalProduct(final Tuple2<RepoDTO, Set<BranchDTO>> source) {
		final var target = source.getT1();
		final var branches = source.getT2();
		target.setBranches(branches);
		return target;
	}
}