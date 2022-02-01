package xyz.jocn.shortenurl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/")
@Controller
public class IndexController {

	@GetMapping
	public String index(Authentication authentication) {
		log.info("index authentication = {}", authentication);
		if (authentication != null) {
			log.info("Authentication type : {}", authentication.getClass());
			log.info("Principal = {}", authentication.getPrincipal());
			log.info("Credentials = {}", authentication.getCredentials());
			log.info("Authorities = {}", authentication.getAuthorities());
			log.info("name = {}", authentication.getName());
		}
		return "index";
	}

	@GetMapping("/test")
	public String test(Authentication authentication) {
		log.info("test authentication = {}", authentication);
		return "test";
	}

	@GetMapping("/vw/login")
	public String login() {
		log.info("login page call");
		return "login";
	}
}
