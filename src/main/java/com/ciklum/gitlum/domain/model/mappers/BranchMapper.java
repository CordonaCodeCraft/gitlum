package com.ciklum.gitlum.domain.model.mappers;

import com.ciklum.gitlum.domain.model.dto.BranchDTO;
import com.ciklum.gitlum.domain.model.git.Branch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BranchMapper {
	@Mapping(source = "name", target = "branchName")
	@Mapping(source = "commit.sha", target = "lastCommitSha")
	BranchDTO ToDTO(final Branch source);
}
