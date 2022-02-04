package xyz.jocn.shortenurl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.shortenurl.url.UrlService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/")
@Controller
public class IndexController {

	private final UrlService urlService;

	@GetMapping
	public String index() {
		return "redirect:/vw/urls";
	}

	@GetMapping("/vw/login")
	public String login() {
		return "login";
	}

	@GetMapping("/{routingId}")
	public String routing(@PathVariable String routingId) {
		return "redirect:" + urlService.route(routingId);
	}

}
