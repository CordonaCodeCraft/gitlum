package com.ciklum.gitlum.domain.usecase;

import com.ciklum.gitlum.config.GitProperties;
import com.ciklum.gitlum.domain.model.git.Branch;
import com.ciklum.gitlum.domain.model.git.Repo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class GetBranches {

  private final GitProperties gitProperties;
  private final WebClient.Builder webClientBuilder;

  public Flux<Branch> invoke(final Repo source, final String token) {
    return webClientBuilder
        .baseUrl(gitProperties.getBaseUrl())
        .build()
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path(gitProperties.getBranchesUri())
                    .build(source.getOwner().getLogin(), source.getName()))
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
