package com.ciklum.gitlum.domain.model.mappers;

import com.ciklum.gitlum.domain.model.dto.RepoDTO;
import com.ciklum.gitlum.domain.model.git.Repo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel="spring")
public interface RepoMapper {
	@Mapping(source = "name", target = "repositoryName")
	@Mapping(source = "owner.login", target = "ownerLogin")
	RepoDTO toDTO(final Repo source);

}
