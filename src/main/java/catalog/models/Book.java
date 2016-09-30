package catalog.models;

import java.util.UUID;

import org.springframework.util.StringUtils;

public class Book {
	private String id;
	private String isbn;
	private String title;
	private String author;
	private int year;
	private String publisher;
	private BookCover cover;
	private String coverColor;
	private boolean ebook;
	private String description;
	private String notes;

	protected Book() {
		this("", "", "");
	}

	public Book(String isbn, String title, String author) {
		this.isbn = isbn;
		this.setTitle(title);
		this.setAuthor(author);
		this.id = StringUtils.isEmpty(isbn) || !isIsbnValid() ? generateUniqueId() : isbn;
	}

	public Book(String isbn, String title, String author, int year, String publisher, BookCover cover, String coverColor, boolean ebook,
			String description, String notes) {
		this(isbn, title, author);
		this.setPublisher(publisher);
		this.setYear(year);
		this.setCover(cover);
		this.setCoverColor(coverColor);
		this.setEbook(ebook);
		this.setDescription(description);
		this.setNotes(notes);
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

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public BookCover getCover() {
		return cover;
	}

	public void setCover(BookCover cover) {
		this.cover = cover;
	}

	public String getCoverColor() {
		return coverColor;
	}

	public void setCoverColor(String coverColor) {
		this.coverColor = coverColor;
	}

	public boolean isEbook() {
		return ebook;
	}

	public void setEbook(boolean ebook) {
		this.ebook = ebook;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
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
