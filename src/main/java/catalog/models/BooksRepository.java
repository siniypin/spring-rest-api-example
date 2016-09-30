package catalog.models;

import java.util.List;

public interface BooksRepository {
	List<Book> getAll();
	Book getById(String id);
	void saveOrUpdate(Book book);
}
