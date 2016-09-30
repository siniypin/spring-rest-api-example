package catalog.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Book show(@PathVariable String id){
		return repository.getById(id);
	}
}