package xyz.jocn.shortenurl.user.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import xyz.jocn.shortenurl.user.UserEntity;
import xyz.jocn.shortenurl.user.dto.UserDto;

@Mapper
public interface UserConverter {
	UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

	@Mapping(source = "entity.createdBy.uid", target = "createdBy")
	@Mapping(source = "entity.updatedBy.uid", target = "updatedBy")
	UserDto toDto(UserEntity entity);
}
