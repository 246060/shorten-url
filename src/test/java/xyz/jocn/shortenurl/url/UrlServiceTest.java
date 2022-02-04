package xyz.jocn.shortenurl.url;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import xyz.jocn.shortenurl.common.dto.PageDto;
import xyz.jocn.shortenurl.url.dto.CreateUrlDto;
import xyz.jocn.shortenurl.url.dto.DeleteUrlDto;
import xyz.jocn.shortenurl.url.dto.UrlDto;
import xyz.jocn.shortenurl.url.enums.UrlState;
import xyz.jocn.shortenurl.url.repo.UrlRepository;
import xyz.jocn.shortenurl.user.UserEntity;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {

	@Mock
	UrlRepository urlRepository;

	@InjectMocks
	UrlService urlService;

	@DisplayName("등록된 URL page")
	@Test
	void getBasePage() {

		// given
		long uid = 1L;
		PageRequest pageRequest = PageRequest.of(0, 10);
		List<UrlEntity> content = new LinkedList<>();
		content.add(UrlEntity.builder().id(uid).build());
		Page<UrlEntity> page = new PageImpl<>(content, pageRequest, 1);

		given(urlRepository.findPageByUserUid(anyLong(), any(Pageable.class)))
			.willReturn(page);

		// when
		PageDto<UrlDto> pageDto = urlService.getBasePage(uid, pageRequest);

		// then
		assertThat(pageDto).isNotNull();
		assertThat(pageDto.getContent()).isNotEmpty();
		assertThat(pageDto.getContent().size()).isEqualTo(1L);
		assertThat(pageDto.getContent()).hasOnlyElementsOfType(UrlDto.class);
		assertThat(pageDto.getPaging()).isNotNull();
	}

	@DisplayName("삭제")
	@Test
	void delete() {

		// given
		long uid = 1L;
		UserEntity user = UserEntity.builder().uid(uid).build();
		UrlEntity url = UrlEntity.builder().createdBy(user).build();

		given(urlRepository.findById(anyLong())).willReturn(Optional.ofNullable(url));

		DeleteUrlDto deleteUrlDto = new DeleteUrlDto();
		deleteUrlDto.setUrlId(1L);
		deleteUrlDto.setUid(uid);

		// when
		urlService.delete(deleteUrlDto);

		// then
		assertThat(url.getState()).isEqualTo(UrlState.DELETED);

		then(urlRepository)
			.should(times(1))
			.findById(anyLong());
	}

	@DisplayName("생성")
	@Test
	void create() {
		// given
		String originUrl = "http://hellworld.com/tiger";
		CreateUrlDto createUrlDto = new CreateUrlDto();
		createUrlDto.setOriginUrl(originUrl);

		// when
		urlService.create(createUrlDto);

		// then
		then(urlRepository)
			.should(times(1))
			.save(any(UrlEntity.class));
	}

	@DisplayName("라우팅")
	@Test
	void route() {
		// given
		String routingId = "hello";
		int clickCnt = 0;
		UrlEntity url = UrlEntity.builder().clickCnt(clickCnt).build();

		given(urlRepository.findByIdAndState(anyLong(), any(UrlState.class)))
			.willReturn(Optional.of(url));

		// when
		urlService.route(routingId);

		// then
		assertThat(url.getClickCnt()).isGreaterThan(clickCnt);

		then(urlRepository)
			.should(times(1))
			.findByIdAndState(anyLong(), any(UrlState.class));
	}
}