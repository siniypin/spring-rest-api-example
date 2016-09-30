package catalog.models;

import java.util.UUID;

import org.springframework.util.StringUtils;

public class Book {
	private String id;
	private String isbn;
	private String title;
	private String author;

	protected Book() {
		this("", "", "");
	}

	public Book(String isbn, String title, String author) {
		this.isbn = isbn;
		this.setTitle(title);
		this.setAuthor(author);
		this.id = StringUtils.isEmpty(isbn) || !isIsbnValid() ? generateUniqueId() : isbn;
	}

	public String getId() {
		return id;
	}

	public String getIsbn() {
		return isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public boolean isValid() {
		return !StringUtils.isEmpty(this.title) && !StringUtils.isEmpty(this.author);
	}

	protected String generateUniqueId() {
		return UUID.randomUUID().toString();
	}

	private boolean isIsbnValid() {
		int numberOfDigits = this.isbn.replaceAll("[^0-9.]", "").length();
		boolean containsNoInvalidChars = this.isbn.replace("-", "").length() == numberOfDigits;
		return containsNoInvalidChars && (numberOfDigits == 9 || numberOfDigits == 10 || numberOfDigits == 13);
	}
}
