package ru.otus.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.domain.*;
import ru.otus.domain.dto.BookCommentDto;
import ru.otus.services.facades.BookCommentsFacade;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookCommentsRestController.class)
@DisplayName("controller for work with comments")
class BookCommentsRestControllerUnitTest {

	private static final String BOOK_NAME = "name";
	private static final long AUTHOR_ID = 1L;
	private static final long GENRE_ID = 1L;
	private static final long BOOK_ID = 1L;
	private static final Date BOOK_PUBLICATION_DATE = new Date();
	private static final int BOOK_PRINT_LENGTH = 300;
	private static final String AUTHOR_NAME = "name";
	private static final String AUTHOR_SURNAME = "surname";
	private static final String GENRE_NAME = "name";
	private static final long BOOK_COMMENT_ID = 1L;
	private static final String BOOK_COMMENT = "comment";
	private static final String BOOK_COMMENT_DOMAIN_URL = "/api/book/comment/";
	private static final String BOOK_COMMENT_BY_ID_URL = BOOK_COMMENT_DOMAIN_URL + "{id}";

	@Autowired
	private MockMvc mvc;

	@MockBean(name = "userDetailsServiceImpl")
	private UserDetailsService userDetailsService;

	@MockBean
	private BookCommentsFacade bookCommentsFacade;

	private InOrder inOrder;

	@Autowired
	private ObjectMapper mapper;

	private BookComment bookComment;

	@BeforeEach
	void setUp() {
		this.inOrder = inOrder(bookCommentsFacade);
		this.bookComment = new BookComment.Builder()
				.id(BOOK_COMMENT_ID)
				.book(buildBook())
				.text(BOOK_COMMENT)
				.build();
	}

	@Test
	@WithMockUser
	@DisplayName("should create comment")
	public void shouldCreateComment() throws Exception {
		var candidate = buildBookCommentCandidate();
		var expectedComment = new BookCommentDto(bookComment);
		var requestBuilder = post(BOOK_COMMENT_DOMAIN_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8.name())
				.content(mapper.writeValueAsString(candidate));

		given(bookCommentsFacade.create(any())).willReturn(expectedComment);

		mvc.perform(requestBuilder)
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(mapper.writeValueAsString(expectedComment)));

		inOrder.verify(bookCommentsFacade, times(1)).create(any());
	}

	@Test
	@WithMockUser
	@DisplayName("should getting comment by id")
	public void shouldGettingCommentById() throws Exception {
		var expectedComment = new BookCommentDto(bookComment);
		var requestBuilder = get(BOOK_COMMENT_BY_ID_URL, BOOK_COMMENT_ID)
				.contentType(MediaType.APPLICATION_JSON);

		given(bookCommentsFacade.getById(BOOK_COMMENT_ID)).willReturn(Optional.of(expectedComment));

		mvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(mapper.writeValueAsString(expectedComment)));

		inOrder.verify(bookCommentsFacade, times(1)).getById(BOOK_COMMENT_ID);
	}

	@Test
	@WithMockUser
	@DisplayName("should return not found status code when book comment by id is not found")
	public void shouldReturnNotFoundStatusCodeWhenBookCommentByIdIsNotFound() throws Exception {
		var requestBuilder = get(BOOK_COMMENT_BY_ID_URL, BOOK_COMMENT_ID)
				.contentType(MediaType.APPLICATION_JSON);

		given(bookCommentsFacade.getById(BOOK_COMMENT_ID)).willReturn(Optional.empty());

		mvc.perform(requestBuilder)
				.andExpect(status().isNotFound())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));

		inOrder.verify(bookCommentsFacade, times(1)).getById(BOOK_COMMENT_ID);
	}

	@Test
	@WithMockUser
	@DisplayName("should getting all comments")
	public void shouldCallGettingAllComments() throws Exception {
		var expectedComments = List.of(new BookCommentDto(bookComment));
		var requestBuilder = get(BOOK_COMMENT_DOMAIN_URL)
				.contentType(MediaType.APPLICATION_JSON);

		given(bookCommentsFacade.getAll()).willReturn(expectedComments);

		mvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(mapper.writeValueAsString(expectedComments)));

		inOrder.verify(bookCommentsFacade, times(1)).getAll();
	}

	@Test
	@WithMockUser
	@DisplayName("should delete comment")
	public void shouldDeleteComment() throws Exception {
		var requestBuilder = delete(BOOK_COMMENT_BY_ID_URL, BOOK_COMMENT_ID)
				.contentType(MediaType.APPLICATION_JSON);

		mvc.perform(requestBuilder).andExpect(status().isNoContent());

		inOrder.verify(bookCommentsFacade, times(1)).deleteById(BOOK_COMMENT_ID);
	}

	@Test
	@WithMockUser
	@DisplayName("should update comment")
	public void shouldUpdateComment() throws Exception {
		var requestBuilder = put(BOOK_COMMENT_DOMAIN_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8.name())
				.content(mapper.writeValueAsString(bookComment));

		mvc.perform(requestBuilder).andExpect(status().isNoContent());

		inOrder.verify(bookCommentsFacade, times(1)).update(any());
	}

	private BookCommentCandidate buildBookCommentCandidate() {
		var candidate = new BookCommentCandidate();

		candidate.setBookId(BOOK_ID);
		candidate.setText(BOOK_COMMENT);

		return candidate;
	}

	private Genre buildGenre() {
		return new Genre.Builder()
				.id(GENRE_ID)
				.name(GENRE_NAME)
				.build();
	}

	private Author buildAuthor() {
		return new Author.Builder()
				.id(AUTHOR_ID)
				.name(AUTHOR_NAME)
				.surname(AUTHOR_SURNAME)
				.build();
	}

	private Book buildBook() {
		return new Book.Builder()
				.id(BOOK_ID)
				.name(BOOK_NAME)
				.publicationDate(BOOK_PUBLICATION_DATE)
				.printLength(BOOK_PRINT_LENGTH)
				.author(buildAuthor())
				.genre(buildGenre())
				.build();
	}
}