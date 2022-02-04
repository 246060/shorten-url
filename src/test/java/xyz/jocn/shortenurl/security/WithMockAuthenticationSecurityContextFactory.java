package xyz.jocn.shortenurl.security;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockAuthenticationSecurityContextFactory
	implements WithSecurityContextFactory<WithMockAuthentication> {

	@Override
	public SecurityContext createSecurityContext(WithMockAuthentication annotation) {

		SecurityContext context = SecurityContextHolder.createEmptyContext();

		Set<SimpleGrantedAuthority> authorities
			= Collections.singleton(new SimpleGrantedAuthority(annotation.role()));

		Map<String, Object> attributes
			= Map.of("uid", annotation.uid(), "email", annotation.email());

		DefaultOAuth2User oAuth2User
			= new DefaultOAuth2User(authorities, attributes, "email");

		OAuth2AuthenticationToken token
			= new OAuth2AuthenticationToken(oAuth2User, authorities, annotation.registrationId());

		context.setAuthentication(token);
		return context;
	}
}
