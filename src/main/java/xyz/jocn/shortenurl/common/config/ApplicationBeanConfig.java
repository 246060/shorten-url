package xyz.jocn.shortenurl.common.config;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.shortenurl.common.service.DefaultOAuth2UserServiceAdapter;
import xyz.jocn.shortenurl.user.UserService;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ApplicationBeanConfig {

	private final Environment environment;

	@PostConstruct
	public void init() {
		for (String defaultProfile : environment.getDefaultProfiles())
			log.info("Default Profile = {}", defaultProfile);
		for (String activeProfile : environment.getActiveProfiles())
			log.info("Active Profile = {}", activeProfile);
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper om = new ObjectMapper();
		om.registerModule(new JavaTimeModule());
		om.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
		return om;
	}

	@Bean
	public OAuth2UserService oAuth2UserService(UserService userService) {
		return new DefaultOAuth2UserServiceAdapter(userService, new DefaultOAuth2UserService());
	}
}
