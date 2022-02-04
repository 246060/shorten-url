package xyz.jocn.shortenurl.common.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import xyz.jocn.shortenurl.common.dto.PageDto;

@Mapper
public interface PagingConverter {

	PagingConverter INSTANCE = Mappers.getMapper(PagingConverter.class);

	default PageDto toDto(Page page) {
		PageDto pageDto = new PageDto();
		pageDto.setContent(page.getContent());

		pageDto.getPaging().setPage(page.getNumber());
		pageDto.getPaging().setSize(page.getSize());
		pageDto.getPaging().setTotalElements(page.getTotalElements());
		pageDto.getPaging().setTotalPages(page.getTotalPages());
		pageDto.getPaging().setPrev(page.hasPrevious());
		pageDto.getPaging().setNext(page.hasNext());

		return pageDto;
	}
}
