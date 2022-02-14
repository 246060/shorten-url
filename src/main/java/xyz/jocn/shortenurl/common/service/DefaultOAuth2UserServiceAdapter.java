package xyz.jocn.shortenurl.common.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.extern.slf4j.Slf4j;
import xyz.jocn.shortenurl.user.UserService;
import xyz.jocn.shortenurl.user.dto.UserDto;
import xyz.jocn.shortenurl.user.enums.ProviderType;

@Slf4j
public class DefaultOAuth2UserServiceAdapter implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final String nameAttributeKey = "email";
	private final String addedUserAttrKey = "uid";

	private UserService userService;
	private OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;

	public DefaultOAuth2UserServiceAdapter(
		UserService userService,
		OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService
	) {
		log.info("DefaultOAuth2UserServiceAdapter instant create");
		this.userService = userService;
		this.oAuth2UserService = oAuth2UserService;
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
		log.debug("{}", userRequest.getClientRegistration().getRegistrationId());
		log.debug("{}", oAuth2User);

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
				// TODO
				break;
		}

		long uid = userService.saveOrUpdate(userAttributes, getProviderType(userRequest), providerUid);
		UserDto user = userService.getUser(uid);
		userAttributes.put(addedUserAttrKey, user.getUid());

		Set<SimpleGrantedAuthority> authorities
			= Collections.singleton(new SimpleGrantedAuthority(user.getRole().getFullName()));

		return new DefaultOAuth2User(authorities, userAttributes, nameAttributeKey);
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
