package catalog.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.util.StringUtils;

import static java.util.Optional.ofNullable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Book {
	private String id;
	private String isbn;
	@NotNull
	@Size(min = 1, max = 255)
	private String title;
	@NotNull
	@Size(min = 1, max = 255)
	private String author;
	private int year;
	private String publisher;
	private BookCover cover;
	private String coverColor;
	private boolean ebook;
	private String description;
	private String notes;
	private int numberOfCopies;

	protected Book() {
		this("", "", "");
	}

	public Book(String isbn, String title, String author) {
		this.isbn = isbn;
		this.setTitle(title);
		this.setAuthor(author);
		this.id = StringUtils.isEmpty(isbn) || !isIsbnValid() ? generateUniqueId() : isbn;
		this.increaseNumberOfCopies();
	}

	public Book(String isbn, String title, String author, int year, String publisher, BookCover cover,
			String coverColor, boolean ebook, String description, String notes) {
		this(isbn, title, author);
		this.setPublisher(publisher);
		this.setYear(year);
		this.setCover(cover);
		this.setCoverColor(coverColor);
		this.setEbook(ebook);
		this.setDescription(description);
		this.setNotes(notes);
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

	protected void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	protected void setAuthor(String author) {
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

	protected void setCover(BookCover cover) {
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

	protected void setEbook(boolean ebook) {
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

	public int getNumberOfCopies() {
		return numberOfCopies;
	}

	public void increaseNumberOfCopies() {
		this.numberOfCopies++;
	}

	@JsonIgnore
	public boolean isValid() {
		return !StringUtils.isEmpty(this.title) && !StringUtils.isEmpty(this.author);
	}

	protected String generateUniqueId() {
		return String.format("%s-%s-%s-%s", ofNullable(title).orElse("").hashCode(),
				ofNullable(author).orElse("").hashCode(), ofNullable(cover).orElse(BookCover.NONE).hashCode(),
				((Boolean) ebook).hashCode());
	}

	private boolean isIsbnValid() {
		int numberOfDigits = this.isbn.replaceAll("[^0-9]", "").length();
		boolean containsNoInvalidChars = this.isbn.replace("-", "").length() == numberOfDigits;
		return containsNoInvalidChars && (numberOfDigits == 9 || numberOfDigits == 10 || numberOfDigits == 13);
	}
}
