package com.ciklum.gitlum.domain.model.mapping;

import com.ciklum.gitlum.domain.model.dto.RepoDTO;
import com.ciklum.gitlum.domain.model.git.Repo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RepoMapper {

	RepoMapper INSTANCE = Mappers.getMapper(RepoMapper.class);

	@Mapping(source = "name", target = "repositoryName")
	@Mapping(source = "owner.login", target = "ownerLogin")
	RepoDTO entityToDTO(final Repo source);

}
