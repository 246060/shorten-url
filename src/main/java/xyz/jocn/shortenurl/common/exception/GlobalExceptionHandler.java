package xyz.jocn.shortenurl.common.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(PageNotFoundException.class)
	public void handlePageNotFoundException(PageNotFoundException ex, HttpServletResponse response)
		throws IOException {
		log.error("{}, {}", ex.getClass().getSimpleName(), ex.getMessage());
		response.sendError(404);
	}

	@ExceptionHandler(ResourcePermissionException.class)
	public void handleResourcePermissionException(ResourcePermissionException ex, HttpServletResponse response)
		throws IOException {
		log.error("{}, {}", ex.getClass().getSimpleName(), ex.getMessage());
		response.sendError(403);
	}
}
