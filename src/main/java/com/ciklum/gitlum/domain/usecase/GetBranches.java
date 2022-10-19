package com.ciklum.gitlum.domain.usecase;

import com.ciklum.gitlum.annotations.UseCase;
import com.ciklum.gitlum.domain.model.dto.BranchDTO;
import com.ciklum.gitlum.domain.model.git.Branch;
import com.ciklum.gitlum.domain.model.git.Repo;
import com.ciklum.gitlum.domain.model.mappers.BranchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

import static com.ciklum.gitlum.config.Constants.REPOSITORY_BRANCHES;

@UseCase
@RequiredArgsConstructor
public class GetBranches {

	private final WebClient.Builder webClientBuilder;
	public final BranchMapper branchMapper;

	public Mono<Set<BranchDTO>> invoke(final Repo source) {
		final var branchesURI = String.format(REPOSITORY_BRANCHES, source.getOwner().getLogin(), source.getName());
		return webClientBuilder
				.build()
				.get()
				.uri(branchesURI)
				.retrieve()
				.bodyToFlux(Branch.class)
				.map(branchMapper::entityToDTO)
				.collect(Collectors.toSet());
	}

}
