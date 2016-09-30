package catalog.models;

public class Book {
	private String id;
	private String isbn;

	public Book() {
		this("");
	}

	public Book(String isbn) {
		this.isbn = isbn;
		this.id = isbn;
	}

	public String getId() {
		return id;
	}

	public String getIsbn() {
		return isbn;
	}

	public boolean isValid() {
		return false;
	}
}
