package xyz.jocn.shortenurl.url;

import static org.springframework.http.MediaType.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.jocn.shortenurl.common.dto.PageDto;
import xyz.jocn.shortenurl.url.dto.CreateUrlDto;
import xyz.jocn.shortenurl.url.dto.DeleteUrlDto;
import xyz.jocn.shortenurl.url.dto.UrlDto;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/vw/urls")
@Controller
public class UrlController {

	private final UrlService urlService;

	private long getUid(Authentication authentication) {
		return ((OAuth2AuthenticationToken)authentication).getPrincipal().getAttribute("uid");
	}

	@GetMapping
	public String page(Authentication authentication, @PageableDefault Pageable pageable, Model model) {
		model.addAttribute("createUrlDto", new CreateUrlDto());
		model.addAttribute("page", urlService.getBasePage(getUid(authentication), pageable));
		return "urls";
	}

	@PostMapping
	public String create(
		Authentication authentication, @PageableDefault Pageable pageable, Model model,
		@Validated CreateUrlDto createUrlDto, BindingResult bindingResult
	) {

		if (bindingResult.hasErrors()) {
			PageDto<UrlDto> pageDto = urlService.getBasePage(getUid(authentication), pageable);
			model.addAttribute("page", pageDto);
			return "urls";
		}

		urlService.create(createUrlDto);
		return "redirect:/";
	}

	@PostMapping(value = "/delete")
	public String delete(Authentication authentication, DeleteUrlDto deleteUrlDto) {
		deleteUrlDto.setUid(getUid(authentication));
		urlService.delete(deleteUrlDto);
		return "redirect:/";
	}

}
