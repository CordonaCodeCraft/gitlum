package com.ciklum.gitlum.domain.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
@ApiModel(value = "Data transfer object for presentation of a Github repo per the business requirements")
public class RepoDTO {
	@ApiModelProperty(value = "The name of the repository", required = true)
	private String repositoryName;
	@ApiModelProperty(value = "The owner's login name", required = true)
	private String ownerLogin;
	@ApiModelProperty(value = "A collection of branches, displayed per the business requirements", required = true)
	private Set<BranchDTO> branches;
}
