package xyz.jocn.shortenurl.url.repo;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.test.context.ActiveProfiles;

import xyz.jocn.shortenurl.common.config.JpaAuditingConfig;
import xyz.jocn.shortenurl.security.WithMockAuthentication;
import xyz.jocn.shortenurl.url.UrlEntity;
import xyz.jocn.shortenurl.url.enums.UrlState;

@WithMockAuthentication
@Import(value = {JpaAuditingConfig.class})
@ActiveProfiles(profiles = {"default", "local"})
@DataJpaTest
class UrlRepositoryImplTest {

	@Autowired
	TestEntityManager em;

	@Autowired
	UrlRepository urlRepository;

	@Test
	void findPage() {
		// given
		long userUid = 5L;

		List<UrlEntity> list = new LinkedList<>();
		for (int i = 0; i < 20; i++) {
			list.add(UrlEntity.builder()
				.originUrl("http://localhost/path" + i)
				.shortenUrl("http://jocn.xyz/" + i)
				.state(UrlState.ACTIVE)
				.build());
		}

		urlRepository.saveAllAndFlush(list);

		PageRequest pageRequest = PageRequest.of(0, 10);

		// when
		Page<UrlEntity> page = urlRepository.findPageByUserUid(userUid, pageRequest);
		page.getContent().forEach(System.out::println);

		// then
		assertThat(page).isNotNull();
		assertThat(page.getContent()).isNotEmpty();
		assertThat(page.getContent().size()).isEqualTo(10);
		assertThat(page.getNumber()).isEqualTo(0);
		assertThat(page.getSize()).isEqualTo(10);
	}
}