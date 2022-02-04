package xyz.jocn.shortenurl.common.config;

import java.util.Optional;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import xyz.jocn.shortenurl.user.UserEntity;

@EnableJpaAuditing
@Configuration
public class JpaAuditingConfig implements AuditorAware<UserEntity> {

	final long WEB_MASTER_UID = 1L;

	@Override
	public Optional<UserEntity> getCurrentAuditor() {
		OAuth2AuthenticationToken authentication
			= (OAuth2AuthenticationToken)SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return Optional.ofNullable(UserEntity.builder().uid(WEB_MASTER_UID).build());
		}

		long uid = authentication.getPrincipal().getAttribute("uid");
		return Optional.ofNullable(UserEntity.builder().uid(uid).build());
	}
}
