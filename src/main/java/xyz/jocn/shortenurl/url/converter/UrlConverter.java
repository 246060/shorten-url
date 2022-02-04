package xyz.jocn.shortenurl.url.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import xyz.jocn.shortenurl.url.UrlEntity;
import xyz.jocn.shortenurl.url.dto.UrlDto;

@Mapper
public interface UrlConverter {

	UrlConverter INSTANCE = Mappers.getMapper(UrlConverter.class);

	UrlDto toUrlDto(UrlEntity entity);
}
