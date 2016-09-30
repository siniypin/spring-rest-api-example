package catalog.models;

import java.util.UUID;

import org.springframework.util.StringUtils;

public class Book {
	private String id;
	private String isbn;

	public Book() {
		this("");
	}

	public Book(String isbn) {
		this.isbn = isbn;
		this.id = StringUtils.isEmpty(isbn) || !isIsbnValid() ? generateUniqueId() : isbn;
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

	protected String generateUniqueId() {
		return UUID.randomUUID().toString();
	}

	private boolean isIsbnValid() {
		int numberOfDigits = this.isbn.replaceAll("[^0-9.]", "").length();
		boolean containsNoInvalidChars = this.isbn.replace("-", "").length() == numberOfDigits;
		return containsNoInvalidChars && (numberOfDigits == 9 || numberOfDigits == 10 || numberOfDigits == 13);
	}
}
