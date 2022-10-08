package com.ciklum.gitlum.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Data transfer object, containing the Github username to be consumed by the controller")
public record Request(
		@ApiModelProperty(value = "Github username", required = true)
		String gitUser
) {
}
