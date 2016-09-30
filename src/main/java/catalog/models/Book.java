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
		this.id = StringUtils.isEmpty(isbn) ? generateUniqueId() : isbn;
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
	
	protected String generateUniqueId(){
		return UUID.randomUUID().toString();
	}
}
