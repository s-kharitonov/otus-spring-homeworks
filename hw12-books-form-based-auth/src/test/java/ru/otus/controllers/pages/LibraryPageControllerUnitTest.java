package ru.otus.controllers.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LibraryPageController.class)
@DisplayName("controller for library page")
class LibraryPageControllerUnitTest {

	private static final String LIBRARY_PAGE_URL = "/library";
	private static final String TEXT_HTML_WITH_CHARSET = TEXT_HTML_VALUE + ";charset=UTF-8";

	@Autowired
	private MockMvc mvc;

	@MockBean(name = "userDetailsServiceImpl")
	private UserDetailsService userDetailsService;

	@Test
	@DisplayName("should return redirect status without auth")
	public void shouldReturnRedirectWithoutAuth() throws Exception {
		var requestBuilder = get(LIBRARY_PAGE_URL).contentType(TEXT_HTML);

		mvc.perform(requestBuilder)
				.andExpect(status().is3xxRedirection())
				.andExpect(header().exists(LOCATION));
	}

	@Test
	@WithMockUser
	@DisplayName("should response redirect without auth")
	public void shouldReturnPageWithAuth() throws Exception {
		var requestBuilder = get(LIBRARY_PAGE_URL).contentType(TEXT_HTML);

		mvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(content().contentType(TEXT_HTML_WITH_CHARSET));
	}
}