package xyz.jocn.shortenurl.url;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import xyz.jocn.shortenurl.common.config.JpaAuditingConfig;
import xyz.jocn.shortenurl.common.dto.PageDto;
import xyz.jocn.shortenurl.common.service.DefaultOAuth2UserServiceAdapter;
import xyz.jocn.shortenurl.security.WithMockAuthentication;
import xyz.jocn.shortenurl.url.dto.CreateUrlDto;
import xyz.jocn.shortenurl.url.dto.DeleteUrlDto;
import xyz.jocn.shortenurl.url.dto.UrlDto;

@WithMockAuthentication
@MockBean(value = {
	DefaultOAuth2UserServiceAdapter.class
})
@WebMvcTest(UrlController.class)
class UrlControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	UrlService urlService;

	@DisplayName("페이징 요청")
	@Test
	void page() throws Exception {
		// given
		PageDto<UrlDto> page = new PageDto<>();
		page.setContent(new LinkedList<>());
		page.getPaging().setNext(false);
		page.getPaging().setPrev(false);
		page.getPaging().setPage(0);
		page.getPaging().setSize(10);
		page.getPaging().setTotalElements(1);

		given(urlService.getBasePage(anyLong(), any(Pageable.class))).willReturn(page);

		MultiValueMap<String, String> queryParam = new LinkedMultiValueMap<>();
		queryParam.add("size", "10");
		queryParam.add("page", "0");

		// when
		ResultActions actions = mockMvc
			.perform(get("/vw/urls")
				.queryParams(queryParam)
			)
			.andDo(print());

		// then
		actions
			.andExpect(status().isOk())
			.andExpect(view().name("urls"))
			.andExpect(handler().methodName("page"))
		;
	}

	@DisplayName("생성 요청")
	@Test
	void create() throws Exception {
		// given
		CreateUrlDto createUrlDto = new CreateUrlDto();
		createUrlDto.setOriginUrl("http://hello-world.com/showmethemoney");

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		Map<String, String> map = new ObjectMapper().convertValue(createUrlDto,
			new TypeReference<Map<String, String>>() {
			});
		params.setAll(map);

		// when
		ResultActions actions = mockMvc
			.perform(post("/vw/urls").with(csrf())
				.contentType(APPLICATION_FORM_URLENCODED)
				.params(params)
			)
			.andDo(print());

		// then
		actions
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/"))
			.andExpect(handler().methodName("create"))
		;
	}
	
	@DisplayName("삭제 요청")
	@Test
	void delete() throws Exception {
		// given
		DeleteUrlDto deleteUrlDto = new DeleteUrlDto();
		deleteUrlDto.setUrlId(1L);

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		Map<String, String> map = new ObjectMapper().convertValue(deleteUrlDto,
			new TypeReference<Map<String, String>>() {
			});
		params.setAll(map);

		// when
		ResultActions actions = mockMvc
			.perform(post("/vw/urls/delete").with(csrf())
				.contentType(APPLICATION_FORM_URLENCODED)
				.params(params)
			)
			.andDo(print());

		// then
		actions
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/"))
			.andExpect(handler().methodName("delete"))
		;
	}
}