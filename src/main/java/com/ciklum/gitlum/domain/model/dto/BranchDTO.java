package com.ciklum.gitlum.domain.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BranchDTO {
	private String branchName;
	private String lastCommitSha;
}
