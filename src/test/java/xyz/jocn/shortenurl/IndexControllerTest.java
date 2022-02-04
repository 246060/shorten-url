package xyz.jocn.shortenurl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import xyz.jocn.shortenurl.common.service.DefaultOAuth2UserServiceAdapter;
import xyz.jocn.shortenurl.security.WithMockAuthentication;
import xyz.jocn.shortenurl.url.UrlService;

@WithMockAuthentication
@MockBean(value = {
	DefaultOAuth2UserServiceAdapter.class
})
@WebMvcTest(IndexController.class)
class IndexControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	UrlService urlService;

	@DisplayName("홈 페이지")
	@Test
	void index() throws Exception {
		// given
		MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
		queryParam.add("page", "0");
		queryParam.add("size", "10");

		// when
		ResultActions actions = mockMvc
			.perform(
				get("/")
					.queryParams(queryParam)
			)
			.andDo(print());

		// then
		actions
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/vw/urls"))
			.andExpect(handler().methodName("index"))
		;
	}

	@DisplayName("로그인 페이지")
	@Test
	void login() throws Exception {
		ResultActions actions = mockMvc
			.perform(get("/vw/login"))
			.andDo(print());

		actions
			.andExpect(status().isOk())
			.andExpect(view().name("login"))
			.andExpect(handler().methodName("login"))
		;
	}

	@DisplayName("라우팅 요청")
	@Test
	void routing() throws Exception {
		// given
		String routingId = "hello";

		// when
		ResultActions actions = mockMvc
			.perform(get("/{routingId}", routingId))
			.andDo(print());

		// then
		actions
			.andExpect(status().is3xxRedirection())
			.andExpect(handler().methodName("routing"))
		;
	}
}