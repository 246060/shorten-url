package xyz.jocn.shortenurl.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockAuthenticationSecurityContextFactory.class)
public @interface WithMockAuthentication {

	long uid() default 5L;

	String email() default "test@jocn.xyz";

	String role() default "ROLE_USER";

	String registrationId() default "google";
}
