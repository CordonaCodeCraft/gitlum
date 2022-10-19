package com.ciklum.gitlum.domain.usecase;

import com.ciklum.gitlum.annotations.UseCase;
import com.ciklum.gitlum.domain.model.dto.BranchDTO;
import com.ciklum.gitlum.domain.model.git.Branch;
import com.ciklum.gitlum.domain.model.git.Repo;
import com.ciklum.gitlum.domain.model.mappers.BranchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@UseCase
@RequiredArgsConstructor
public class GetBranches {

	@Value("${constants.git-base-url}")
	private String gitBaseURL;
	@Value("${constants.repository-branches-uri}")
	private String repositoryBranchesURI;

	private final WebClient.Builder webClientBuilder;
	public final BranchMapper branchMapper;

	public Mono<Set<BranchDTO>> invoke(final Repo source) {
		return webClientBuilder
				.baseUrl(gitBaseURL)
				.build()
				.get()
				.uri(uriBuilder ->
						uriBuilder
								.path(repositoryBranchesURI)
								.build(source.getOwner().getLogin(), source.getName())
				)
				.retrieve()
				.bodyToFlux(Branch.class)
				.map(branchMapper::entityToDTO)
				.collect(Collectors.toSet());
	}
}
