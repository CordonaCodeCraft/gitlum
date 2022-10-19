package com.ciklum.gitlum.domain.usecase;

import com.ciklum.gitlum.annotations.UseCase;
import com.ciklum.gitlum.domain.model.dto.BranchDTO;
import com.ciklum.gitlum.domain.model.dto.RepoDTO;
import com.ciklum.gitlum.domain.model.mappers.RepoMapper;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@UseCase
@RequiredArgsConstructor
public class ProduceResult {

	private final GetRepositories getRepositories;
	private final GetBranches getBranches;
	private final RepoMapper repoMapper;

	public Flux<RepoDTO> invoke(final String userName) {
		return getRepositories
				.invoke(userName)
				.flatMap(repo -> {
							final Mono<RepoDTO> dto = Mono.just(repoMapper.toDTO(repo));
							final Mono<Set<BranchDTO>> branches = getBranches.invoke(repo);
							return dto
									.zipWith(branches)
									.map(tuple -> {
												final var target = tuple.getT1();
												final var source = tuple.getT2();
												target.setBranches(source);
												return target;
											}
									);
						}
				);
	}
}
