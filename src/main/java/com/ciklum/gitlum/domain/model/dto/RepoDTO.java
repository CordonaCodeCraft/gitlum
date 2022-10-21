package com.ciklum.gitlum.domain.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class RepoDTO {
	private String repositoryName;
	private String ownerLogin;
	private Set<BranchDTO> branches;
}
