package com.ciklum.gitlum.domain.model.mapping;

import com.ciklum.gitlum.domain.model.dto.BranchDTO;
import com.ciklum.gitlum.domain.model.git.Branch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BranchMapper {

	BranchMapper INSTANCE = Mappers.getMapper(BranchMapper.class);

	@Mapping(source = "name", target = "branchName")
	@Mapping(source = "commit.sha", target = "lastCommitSha")
	BranchDTO entityToDTO(final Branch source);

}
