package xyz.jocn.shortenurl.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.shortenurl.common.service.DefaultOAuth2UserServiceAdapter;
import xyz.jocn.shortenurl.user.repo.UserRepository;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final OAuth2UserService oAuth2UserService;

	@Override
	public void configure(WebSecurity web) throws Exception {
		// web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
		// web.ignoring().mvcMatchers("/image/**");
		// web.ignoring().antMatchers("/h2-console/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
			.antMatchers("/vw/login").permitAll()
			.antMatchers("/", "/vw/**").authenticated()
			.anyRequest().permitAll()

			.and()
			.logout()
			.logoutSuccessUrl("/")

			.and()
			.oauth2Login()
			.loginPage("/vw/login")
			.defaultSuccessUrl("/")
			.failureUrl("/vw/login")
			.userInfoEndpoint()
			.userService(oAuth2UserService)
		;
	}
}
