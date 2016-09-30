package catalog.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;
import java.util.List;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import catalog.Application;
import catalog.models.Book;
import catalog.models.BooksRepository;
import org.junit.Assert;

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
	private Book bookWithoutIsbn;
	private Book bookWithIsbn;

	private MockMvc mockMvc;

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
		List<Book> books = repository.getAll();
		bookWithoutIsbn = books.get(0);
		bookWithIsbn = books.get(1);
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
	public void shouldIncreaseNumberOfCopiesOfBookWithIsbn() throws Exception {
		// arrange
		List<Book> booksBefore = repository.getAll();
		int numberOfCopiesBefore = bookWithIsbn.getNumberOfCopies();
		int before = booksBefore.size();
		String bookJson = toJson(bookWithIsbn);

		// act
		// assert
		mockMvc.perform(post("/catalog/books").contentType(contentType).content(bookJson)).andExpect(status().isOk())
				.andExpect(content().contentType(contentType)).andDo(new ResultHandler() {
					public void handle(MvcResult result) {
						Assert.assertEquals(before, repository.getAll().size());
						Assert.assertEquals(numberOfCopiesBefore + 1, repository.getById(bookWithIsbn.getId()).getNumberOfCopies());
					}
				});
	}
	
	@Test
	public void shouldIncreaseNumberOfCopiesOfBookWithoutIsbn() throws Exception {
		// arrange
		List<Book> booksBefore = repository.getAll();
		int numberOfCopiesBefore = bookWithoutIsbn.getNumberOfCopies();
		int before = booksBefore.size();
		String bookJson = toJson(bookWithoutIsbn);

		// act
		// assert
		mockMvc.perform(post("/catalog/books").contentType(contentType).content(bookJson)).andExpect(status().isOk())
				.andExpect(content().contentType(contentType)).andDo(new ResultHandler() {
					public void handle(MvcResult result) {
						Assert.assertEquals(before, repository.getAll().size());
						Assert.assertEquals(numberOfCopiesBefore + 1, repository.getById(bookWithoutIsbn.getId()).getNumberOfCopies());
					}
				});
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
		String bookJson = toJson(new Book(bookWithIsbn.getIsbn(), UUID.randomUUID().toString(), bookWithIsbn.getAuthor()));
		mockMvc.perform(put("/catalog/books/" + bookWithIsbn.getId()).contentType(contentType).content(bookJson))
				.andExpect(status().isForbidden());
	}

	@Test
	public void shouldReplyWith403IfAuthorUpdated() throws Exception {
		String bookJson = toJson(new Book(bookWithIsbn.getIsbn(), bookWithIsbn.getTitle(), UUID.randomUUID().toString()));
		mockMvc.perform(put("/catalog/books/" + bookWithIsbn.getId()).contentType(contentType).content(bookJson))
				.andExpect(status().isForbidden());
	}

	@Test
	public void shouldReplyWith403IfIsbnUpdated() throws Exception {
		String bookJson = toJson(new Book(UUID.randomUUID().toString(), bookWithIsbn.getTitle(), bookWithIsbn.getAuthor()));
		mockMvc.perform(put("/catalog/books/" + bookWithIsbn.getId()).contentType(contentType).content(bookJson))
				.andExpect(status().isForbidden());
	}
	
	@Test
	public void shouldReplyWith404IfBookToDeleteNotFound() throws Exception {
		mockMvc.perform(delete("/catalog/books/missing").contentType(contentType)).andExpect(status().isNotFound());
	}

	@Test
	public void shouldDeleteABookSuccessfully() throws Exception {
		mockMvc.perform(delete("/catalog/books/" + repository.getAll().get(2).getId()).contentType(contentType))
				.andExpect(status().isOk());
	}

	private String toJson(Book book) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(book);
	}
}
