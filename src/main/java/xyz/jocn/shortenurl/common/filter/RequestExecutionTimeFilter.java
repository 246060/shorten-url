package xyz.jocn.shortenurl.common.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE - 1)
@Component
public class RequestExecutionTimeFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {

		log.debug("RequestInOutExecutionTimeFilter call");

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		try {
			filterChain.doFilter(request, response);
		} finally {
			stopWatch.stop();
			log.debug("Time Taken : {} ms, Target : {}", stopWatch.getTotalTimeMillis(), MDC.get("TraceId"));
		}
	}
}