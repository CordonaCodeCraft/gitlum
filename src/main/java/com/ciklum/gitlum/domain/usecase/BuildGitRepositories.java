package com.ciklum.gitlum.domain.usecase;

import com.ciklum.gitlum.domain.model.dto.BranchDTO;
import com.ciklum.gitlum.domain.model.dto.RepoDTO;
import com.ciklum.gitlum.domain.model.git.Branch;
import com.ciklum.gitlum.domain.model.git.Repo;
import com.ciklum.gitlum.domain.model.mappers.BranchMapper;
import com.ciklum.gitlum.domain.model.mappers.RepoMapper;
import com.ciklum.gitlum.routes.dto.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuildGitRepositories {

	private final GetRepositories getRepositories;
	private final GetBranches getBranches;
	private final RepoMapper repoMapper;
	private final BranchMapper branchMapper;

	public Flux<RepoDTO> invoke(final Request request) {
		return getRepositories
				.invoke(request)
				.flatMap(r -> repoToDTO(r, request.token()));
	}

	private Mono<RepoDTO> repoToDTO(final Repo source, final String token) {
		final var dto = Mono.just(repoMapper.toDTO(source));
		final var branches = branchesToCollection(getBranches.invoke(source, token));
		return dto
				.zipWith(branches)
				.map(BuildGitRepositories::asFinalProduct);
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