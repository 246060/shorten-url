package xyz.jocn.shortenurl.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class IncomingLogFilterConfig {

	@Profile("!prod")
	@Bean
	public CommonsRequestLoggingFilter localCommonsRequestLoggingFilterFilter() {
		log.info("load local CommonsRequestLoggingFilter");
		CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
		filter.setIncludeClientInfo(true); // 클라이언트 주소와 세션 ID를 로그에 출력
		filter.setIncludeQueryString(true);
		filter.setIncludePayload(true);
		filter.setMaxPayloadLength(10000);
		filter.setIncludeHeaders(true);
		filter.setBeforeMessagePrefix("START --- ");
		filter.setAfterMessagePrefix("END --- ");
		return filter;
	}

	@Profile("prod")
	@Bean
	public CommonsRequestLoggingFilter devCommonsRequestLoggingFilterFilter() {
		log.info("load dev CommonsRequestLoggingFilter");
		CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
		filter.setIncludeClientInfo(true); // 클라이언트 주소와 세션 ID를 로그에 출력
		filter.setIncludeQueryString(true);
		filter.setBeforeMessagePrefix("START --- ");
		filter.setAfterMessagePrefix("END --- ");
		return filter;
	}
}
