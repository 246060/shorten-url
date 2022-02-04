package xyz.jocn.shortenurl.url;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.shortenurl.common.aop.ParameterLog;
import xyz.jocn.shortenurl.common.converter.PagingConverter;
import xyz.jocn.shortenurl.common.dto.PageDto;
import xyz.jocn.shortenurl.common.exception.PageNotFoundException;
import xyz.jocn.shortenurl.common.exception.ResourceNotFoundException;
import xyz.jocn.shortenurl.common.exception.ResourcePermissionException;
import xyz.jocn.shortenurl.core.Base62;
import xyz.jocn.shortenurl.url.converter.UrlConverter;
import xyz.jocn.shortenurl.url.dto.CreateUrlDto;
import xyz.jocn.shortenurl.url.dto.DeleteUrlDto;
import xyz.jocn.shortenurl.url.dto.UrlDto;
import xyz.jocn.shortenurl.url.enums.UrlState;
import xyz.jocn.shortenurl.url.repo.UrlRepository;
import xyz.jocn.shortenurl.user.UserEntity;
import xyz.jocn.shortenurl.user.repo.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UrlService {

	@Value("${app.domain}")
	private String domain;

	private final UrlRepository urlRepository;

	public PageDto<UrlDto> getBasePage(long uid, Pageable pageable) {
		return PagingConverter.INSTANCE.toDto(
			urlRepository
				.findPageByUserUid(uid, pageable)
				.map(UrlConverter.INSTANCE::toUrlDto)
		);
	}

	@Transactional
	public void delete(DeleteUrlDto deleteUrlDto) {
		UrlEntity entity =
			urlRepository
				.findById(deleteUrlDto.getUrlId())
				.orElseThrow(ResourceNotFoundException::new);

		if (entity.getCreatedBy().getUid() != deleteUrlDto.getUid()) {
			throw new ResourcePermissionException("접근 권한 없음");
		}

		entity.delete();
	}

	@ParameterLog
	@Transactional
	public void create(CreateUrlDto createUrlDto) {
		UrlEntity urlEntity
			= UrlEntity.builder()
			.originUrl(createUrlDto.getOriginUrl())
			.state(UrlState.ACTIVE)
			.build();

		urlRepository.save(urlEntity);
		urlEntity.createShortenUrl(domain);
	}

	@Transactional
	public String route(String routingId) {
		return urlRepository
			.findByIdAndState(Base62.decode(routingId), UrlState.ACTIVE)
			.orElseThrow(PageNotFoundException::new)
			.route();
	}
}
