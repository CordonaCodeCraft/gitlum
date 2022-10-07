package com.ciklum.gitlum.domain.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(value = "Data transfer object for presentation of a Github repo branch per the business requirements")
public class BranchDTO {
	@ApiModelProperty(value = "The name of the branch", required = true)
	private String branchName;
	@ApiModelProperty(value = "The value of the last commit sha", required = true)
	private String lastCommitSha;
}
