package xyz.jocn.shortenurl.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final OAuth2UserService oAuth2UserService;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
		// web.ignoring().mvcMatchers("/image/**");
		web.ignoring().antMatchers("/h2-console/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
			.antMatchers("/vw/login").permitAll()
			.antMatchers("/h2-console/**").permitAll()
			.anyRequest().authenticated()

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
