package catalog.models;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

public class BookTest {

	@Test
	public void shouldBePossibleToCreateEmpty() {
		// arrange
		Exception ex = null;

		// act
		try {
			new Book();
		} catch (Exception e) {
			ex = e;
		}

		// assert
		Assert.assertNull(ex);
	}

	@Test
	public void emptyBookShouldNotBeValid() {
		// arrange
		Book sut = new Book();

		// act
		boolean isValid = sut.isValid();

		// assert
		Assert.assertFalse(isValid);
	}

	@Test
	public void shouldUseISBNAsId() {
		// arrange
		String isbn = "978-3-16-148410-0";
		Book sut = new Book(isbn, "", "");

		// act
		String id = sut.getId();

		// assert
		Assert.assertEquals(isbn, id);
	}

	@Test
	public void shouldGenerateUUIDIfISBNIsEmpty() {
		// arrange
		// https://www.uuidgenerator.net/version1
		Book sut = new Book("", "", "");

		// act
		String id = sut.getId();

		// assert
		Assert.assertNotEquals("", id);
	}

	@Test
	public void shouldGenerateUniqueIds() {
		// arrange
		Book sut = new Book("", "", "");
		Book anotherSut = new Book("", "", "");

		// act
		String id1 = sut.getId();
		String id2 = anotherSut.getId();

		// assert
		Assert.assertNotEquals(id1, id2);
	}

	@Test
	public void shouldGenerateUUIDIfISBNIsIncorrect() {
		// arrange
		String isbn = "978-3-16-148410-";
		Book sut = new Book(isbn, "", "");

		// act
		String id = sut.getId();

		// assert
		Assert.assertNotEquals(isbn, id);
	}

	@Test
	public void shouldBeInvalidWithoutTitle() {
		// arrange
		Book sut = new Book("", "", UUID.randomUUID().toString());

		// act
		// assert
		Assert.assertFalse(sut.isValid());
	}

	@Test
	public void shouldBeInvalidWithoutAuthor() {
		// arrange
		Book sut = new Book("", UUID.randomUUID().toString(), "");

		// act
		// assert
		Assert.assertFalse(sut.isValid());
	}
	
	@Test
	public void shouldBeValidWithAllFieldsProvided() {
		// arrange
		String randomTitle = UUID.randomUUID().toString();
		String randomAuthor = UUID.randomUUID().toString();
		Book sut = new Book("", randomTitle, randomAuthor);

		// act
		// assert
		Assert.assertTrue(sut.isValid());
	}
}
