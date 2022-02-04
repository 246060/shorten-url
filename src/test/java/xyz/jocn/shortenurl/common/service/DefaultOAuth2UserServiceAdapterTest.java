package xyz.jocn.shortenurl.common.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

import xyz.jocn.shortenurl.user.repo.UserRepository;

@ExtendWith(MockitoExtension.class)
class DefaultOAuth2UserServiceAdapterTest {

	@Mock
	UserRepository userRepository;

	@Mock
	OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;

	@InjectMocks
	DefaultOAuth2UserServiceAdapter defaultOAuth2UserServiceAdapter;

	@Test
	void loadUser() {
		// given

		// when

		// then
	}

	@Test
	void testLoadUser() {
		// given

		// when

		// then
	}
}