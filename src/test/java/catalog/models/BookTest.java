package catalog.models;

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
		Book sut = new Book(isbn);

		// act
		String id = sut.getId();

		// assert
		Assert.assertEquals(isbn, id);
	}

	@Test
	public void shouldGenerateUUIDIfISBNIsEmpty() {
	}

	@Test
	public void shouldGenerateUUIDIfISBNIsIncorrect() {
	}
}
