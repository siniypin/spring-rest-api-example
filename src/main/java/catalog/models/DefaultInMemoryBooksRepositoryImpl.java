package catalog.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultInMemoryBooksRepositoryImpl implements BooksRepository {
	private Map<String, Book> books;

	public DefaultInMemoryBooksRepositoryImpl() {
		ArrayList<Book> hardCodedBooks = new ArrayList<Book>() {
			{
				add(new Book("", "The Lord of the Rings", "J.R.R.Tolkien", 1998, "S-Z", BookCover.HARD, "", false, "",
						"favourite"));
				add(new Book("978-1567314137", "Millionaire's Path", "Mark Fisher", 2000, "MJF Books", BookCover.HARD,
						"", false, "", "rubbish"));
				add(new Book("978-1567314134", "Millionaire's Path", "Mark Fisher", 2000, "MJF Books", BookCover.NONE,
						"", true, "", "rubbish"));
			}
		};
		books = hardCodedBooks.stream().collect(Collectors.<Book, String, Book> toMap(x -> x.getId(), x -> x));
	}

	@Override
	public List<Book> getAll() {
		return new ArrayList<Book>(books.values());
	}

	@Override
	public Book getById(String id) {
		return books.get(id);
	}

	@Override
	public Book saveOrUpdate(Book book) {
		books.put(book.getId(), book);
		return book;
	}

	@Override
	public void delete(Book book) {
		books.remove(book.getId());
	}
}
