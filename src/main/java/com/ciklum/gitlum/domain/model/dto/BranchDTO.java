package com.ciklum.gitlum.domain.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Data transfer object for presentation of a Github repo branch per the business requirements")
public record BranchDTO(
		@ApiModelProperty(value = "The name of the branch", required = true)
		String branchName,
		@ApiModelProperty(value = "The value of the last commit sha", required = true)
		String lastCommitSha
) {

}
