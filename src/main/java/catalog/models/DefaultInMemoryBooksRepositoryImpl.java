package catalog.models;

import java.util.ArrayList;
import java.util.List;

public class DefaultInMemoryBooksRepositoryImpl implements BooksRepository {
	private ArrayList<Book> books = new ArrayList<Book>() {
		{
			add(new Book("", "The Lord of the Rings", "J.R.R.Tolkien", 1998, "S-Z", BookCover.HARD, "", false, "", "favourite"));
			add(new Book("978-1567314137", "Millionaire's Path", "Mark Fisher", 2000, "MJF Books", BookCover.HARD, "", false, "", "rubbish"));
			add(new Book("978-1567314134", "Millionaire's Path", "Mark Fisher", 2000, "MJF Books", null, "", true, "", "rubbish"));
		}
	};

	@Override
	public List<Book> getAll() {
		return books;
	}

	@Override
	public Book getById(String id) {
		return books.stream().filter(x -> x.getId().equals(id)).findFirst().get();
	}

	@Override
	public void saveOrUpdate(Book book) {
		int index = books.indexOf(getById(book.getId()));
		books.set(index, book);
	}
}
