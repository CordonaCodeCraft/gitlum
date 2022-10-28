package com.ciklum.gitlum.domain.usecase;

import com.ciklum.gitlum.config.GitProperties;
import com.ciklum.gitlum.config.RequestProperties;
import com.ciklum.gitlum.domain.model.git.Branch;
import com.ciklum.gitlum.domain.model.git.Repo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class GetBranches {

  private static final String LAST_PAGE = "Last page";

  private final RequestProperties requestProperties;
  private final GitProperties gitProperties;
  private final WebClient.Builder webClient;

  /**
   * The GitHub API paginates the results even when pagination parameters are not provided. More
   * details on the basics of pagination are provided <a
   * href="https://docs.github.com/en/enterprise-cloud@latest/rest/guides/traversing-with-pagination">here</a>
   * Listing branches also support pagination. The default values are the first page and 30 results
   * per page. The default maximum value for the results per page is "100" as explained <a
   * href="https://docs.github.com/en/enterprise-cloud@latest/rest/branches/branches">here</a>.
   * Suppose a given repository has more than 100 branches. When attempting to get all the branches
   * for this repository - we will end up with only the first 100 branches - the maximum results
   * allowed per page. Therefore - the below method provides an asynchronous accumulation of all the
   * branches across all the pages and returns them as a single {@link Flux} of {@link Branch}
   * objects
   */
  public Flux<Branch> invoke(final Repo source, final String token) {
    return fetchBranches(webClient, toFirstPage(source), token)
        .expand(
            response -> {
              final var headers = response.getHeaders().get(gitProperties.getPaginationHeaderKey());
              if (headers == null) return Mono.empty();
              final var nextPage = toNextPage(headers.get(0));
              return nextPage.equals(LAST_PAGE)
                  ? Mono.empty()
                  : fetchBranches(webClient, nextPage, token);
            })
        .flatMap(HttpEntity::getBody);
  }

  private Mono<ResponseEntity<Flux<Branch>>> fetchBranches(
      final WebClient.Builder webClient, final String link, final String token) {
    return webClient
        .build()
        .get()
        .uri(link)
        .headers(setHeaders(token))
        .retrieve()
        .toEntityFlux(Branch.class);
  }

  private static Consumer<HttpHeaders> setHeaders(final String token) {
    return token.isEmpty()
        ? HttpHeaders::clearContentHeaders
        : HttpHeaders -> HttpHeaders.setBearerAuth(token);
  }

  private String toFirstPage(final Repo source) {
    return String.format(
        gitProperties.getBranchesUriPattern(),
        gitProperties.getBaseUrl(),
        source.getOwner().getLogin(),
        source.getName(),
        requestProperties.getDefaultPageNumber(),
        gitProperties.getMaxResultsPerPageForBranchesAllowed());
  }

  private String toNextPage(final String subject) {
    final var matcher = Pattern.compile(gitProperties.getNextBranchPageRegex()).matcher(subject);
    return matcher.find()
        ? matcher.group().substring(1, matcher.group().lastIndexOf(">"))
        : LAST_PAGE;
  }
}
