package catalog.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import catalog.models.Book;
import catalog.models.BooksRepository;

@RestController
@RequestMapping("/catalog/books")
public class BooksController {
	@Autowired
	private BooksRepository repository;

	@RequestMapping(method = RequestMethod.GET)
	public List<Book> index() {
		return repository.getAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	public Book create(@Validated @RequestBody Book book) {
		Book existingBook = repository.getById(book.getId());
		if (existingBook == null) {
			return repository.saveOrUpdate(book);
		} else {
			existingBook.increaseNumberOfCopies();
			return repository.saveOrUpdate(existingBook);
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Book show(@PathVariable String id) {
		Book book = repository.getById(id);
		if (book == null)
			throw new BookNotFoundException(id);
		return book;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public Book update(@PathVariable String id, @Validated @RequestBody Book book) {
		Book existingBook = repository.getById(id);
		if (existingBook == null)
			throw new BookNotFoundException(id);
		else if (!existingBook.getTitle().equals(book.getTitle()) || !existingBook.getAuthor().equals(book.getAuthor()) ||
				(!StringUtils.isEmpty(existingBook.getIsbn()) && !existingBook.getIsbn().equals(book.getIsbn()))) {
			throw new ImmutablePropertiesUpdatedException();
		} else {
			return repository.saveOrUpdate(book);
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable String id) {
		Book book = repository.getById(id);
		if (book == null)
			throw new BookNotFoundException(id);
		else
			repository.delete(book);
		return new ResponseEntity(HttpStatus.OK);
	}
}