package catalog.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import catalog.Application;
import catalog.models.Book;
import catalog.models.BooksRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class BooksControllerTest {
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private BooksRepository repository;

	private MockMvc mockMvc;

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void shouldReplyWithBooksCollection() throws Exception {
		mockMvc.perform(get("/catalog/books/")).andExpect(status().isOk())
				.andExpect(content().contentType(contentType));
	}

	@Test
	public void shouldReplyWith404IfBookCannotBeFound() throws Exception {
		mockMvc.perform(get("/catalog/books/missing")).andExpect(status().isNotFound());
	}

	@Test
	public void shouldSaveCorrectBook() throws Exception {
		String bookJson = toJson(new Book("", UUID.randomUUID().toString(), UUID.randomUUID().toString()));
		mockMvc.perform(post("/catalog/books").contentType(contentType).content(bookJson)).andExpect(status().isOk())
				.andExpect(content().contentType(contentType));
	}

	@Test
	public void shouldReplyWith400WithoutTitle() throws Exception {
		String bookJson = toJson(new Book("", "", UUID.randomUUID().toString()));
		mockMvc.perform(post("/catalog/books").contentType(contentType).content(bookJson))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void shouldReplyWith400WithoutAuthor() throws Exception {
		String bookJson = toJson(new Book("", UUID.randomUUID().toString(), ""));
		mockMvc.perform(post("/catalog/books").contentType(contentType).content(bookJson))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void shouldReplyWith404IfBookToUpdateNotFound() throws Exception {
		Book book = new Book(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString());
		String bookJson = toJson(book);
		mockMvc.perform(put("/catalog/books/" + book.getId()).contentType(contentType).content(bookJson))
				.andExpect(status().isNotFound());
	}

	@Test
	public void shouldReplyWith403IfTitleUpdated() throws Exception {
		Book book = repository.getAll().get(1);
		String bookJson = toJson(new Book(book.getIsbn(), UUID.randomUUID().toString(), book.getAuthor()));
		mockMvc.perform(put("/catalog/books/" + book.getId()).contentType(contentType).content(bookJson))
				.andExpect(status().isForbidden());
	}

	@Test
	public void shouldReplyWith403IfAuthorUpdated() throws Exception {
		Book book = repository.getAll().get(1);
		String bookJson = toJson(new Book(book.getIsbn(), book.getAuthor(), UUID.randomUUID().toString()));
		mockMvc.perform(put("/catalog/books/" + book.getId()).contentType(contentType).content(bookJson))
				.andExpect(status().isForbidden());
	}

	@Test
	public void shouldReplyWith404IfBookToDeleteNotFound() throws Exception {
		mockMvc.perform(delete("/catalog/books/missing").contentType(contentType)).andExpect(status().isNotFound());
	}

	@Test
	public void shouldDeleteABookSuccessfully() throws Exception {
		mockMvc.perform(delete("/catalog/books/" + repository.getAll().get(0).getId()).contentType(contentType))
				.andExpect(status().isOk());
	}

	private String toJson(Book book) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(book);
	}
}
