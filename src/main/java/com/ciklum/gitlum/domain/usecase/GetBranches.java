package com.ciklum.gitlum.domain.usecase;

import com.ciklum.gitlum.annotations.UseCase;
import com.ciklum.gitlum.domain.model.git.Branch;
import com.ciklum.gitlum.domain.model.git.Repo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@UseCase
@RequiredArgsConstructor
public class GetBranches {

	@Value("${constants.git-base-url}")
	private String gitBaseURL;
	@Value("${constants.repository-branches-uri}")
	private String repositoryBranchesURI;

	private final WebClient.Builder webClientBuilder;

	public Flux<Branch> invoke(final Repo source, final String token) {
		return webClientBuilder
				.baseUrl(gitBaseURL)
				.build()
				.get()
				.uri(uriBuilder ->
						uriBuilder
								.path(repositoryBranchesURI)
								.build(source.getOwner().getLogin(), source.getName())
				)
				.headers(setHeaders(token))
				.retrieve()
				.bodyToFlux(Branch.class);
	}

	private static Consumer<HttpHeaders> setHeaders(final String token) {
		return token.isEmpty()
				? HttpHeaders::clearContentHeaders
				: HttpHeaders -> HttpHeaders.setBearerAuth(token);
	}

}
