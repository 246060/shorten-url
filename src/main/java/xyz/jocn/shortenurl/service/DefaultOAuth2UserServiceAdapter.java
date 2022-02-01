package xyz.jocn.shortenurl.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import xyz.jocn.shortenurl.entity.UserEntity;
import xyz.jocn.shortenurl.enums.ProviderType;
import xyz.jocn.shortenurl.enums.RoleType;
import xyz.jocn.shortenurl.repo.UserRepository;

@Slf4j
@Component
public class DefaultOAuth2UserServiceAdapter implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private UserRepository userRepository;
	private OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;

	public DefaultOAuth2UserServiceAdapter(UserRepository userRepository) {
		log.info("DefaultOAuth2UserServiceAdapter instant create");
		this.userRepository = userRepository;
		oAuth2UserService = new DefaultOAuth2UserService();
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
		log.info("{}", userRequest.getClientRegistration().getRegistrationId());
		log.info("{}", oAuth2User);

		String providerUid = null;
		Map<String, Object> userAttributes = new HashMap<>();
		switch (getProviderType(userRequest)) {
			case GOOGLE:
				userAttributes.put("name", oAuth2User.getAttribute("name"));
				userAttributes.put("email", oAuth2User.getAttribute("email"));
				userAttributes.put("picture", oAuth2User.getAttribute("picture"));
				providerUid = oAuth2User.getAttribute(getUserNameAttributeName(userRequest));
				break;
			case FACEBOOK:
				userAttributes.put("name", oAuth2User.getAttribute("name"));
				userAttributes.put("email", oAuth2User.getAttribute("email"));
				userAttributes.put("picture", null);
				providerUid = oAuth2User.getAttribute(getUserNameAttributeName(userRequest));
				break;
			case NAVER:
				userAttributes.put("name", ((Map)oAuth2User.getAttribute("response")).get("name"));
				userAttributes.put("email", ((Map)oAuth2User.getAttribute("response")).get("email"));
				userAttributes.put("picture", ((Map)oAuth2User.getAttribute("response")).get("profile_image"));
				providerUid = (String)((Map)oAuth2User.getAttribute("response")).get("id");
				break;
			case KAKAO:
				break;
		}

		String name = (String)userAttributes.get("name");
		String email = (String)userAttributes.get("email");
		String picture = (String)userAttributes.get("picture");
		String finalProviderUid = providerUid;

		UserEntity user = userRepository
			.findByEmail(email)
			.map(userEntity -> {
				userEntity.oauthLogin(name, email, picture, getProviderType(userRequest), finalProviderUid);
				return userEntity;
			})
			.orElse(
				UserEntity.builder()
					.name(name)
					.email(email)
					.picture(picture)
					.providerType(getProviderType(userRequest))
					.providerUid(finalProviderUid)
					.role(RoleType.USER)
					.build()
			);

		userRepository.save(user);
		userAttributes.put("uid", user.getUid());

		return new DefaultOAuth2User(
			Collections.singleton(new SimpleGrantedAuthority(user.getRole().getFullName())),
			userAttributes,
			"email"
		);
	}

	private ProviderType getProviderType(OAuth2UserRequest userRequest) {
		return ProviderType.valueOf(getRegistrationId(userRequest).toUpperCase());
	}

	private String getRegistrationId(OAuth2UserRequest userRequest) {
		return userRequest.getClientRegistration().getRegistrationId();
	}

	private String getUserNameAttributeName(OAuth2UserRequest userRequest) {
		return userRequest
			.getClientRegistration()
			.getProviderDetails()
			.getUserInfoEndpoint()
			.getUserNameAttributeName();
	}

}
