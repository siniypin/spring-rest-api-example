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
	public void shouldGenerateConsistentUIDIfISBNIsEmpty() {
		// arrange
		String randomTitle = UUID.randomUUID().toString();
		String randomAuthor = UUID.randomUUID().toString();
		BookCover cover = BookCover.HARD;
		boolean ebook = false;
		Book sut = new Book("", randomTitle, randomAuthor, 1999, "", cover, "", ebook, "", "");
		Book anotherSut = new Book("", randomTitle, randomAuthor, 1999, "", cover, "", ebook, "", "");
		String expectedId = String.format("%s-%s-%s-%s", randomTitle.hashCode(), randomAuthor.hashCode(),
				cover.hashCode(), ((Boolean) ebook).hashCode());

		// act
		String id = sut.getId();
		String anotherId = sut.getId();

		// assert
		Assert.assertEquals(expectedId, id);
		Assert.assertEquals(expectedId, anotherId);
	}

	@Test
	public void shouldGenerateUniqueIdsForDifferentBooks() {
		// arrange
		Book sut = new Book("", UUID.randomUUID().toString(), UUID.randomUUID().toString());
		Book anotherSut = new Book("", UUID.randomUUID().toString(), UUID.randomUUID().toString());

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

	@Test
	public void shouldBeValidIfOtherPropsNotProvided() {
		// arrange
		String randomTitle = UUID.randomUUID().toString();
		String randomAuthor = UUID.randomUUID().toString();
		Book sut = new Book("", randomTitle, randomAuthor, 0, "", null, null, false, null, null);

		// act
		// assert
		Assert.assertTrue(sut.isValid());
	}

	@Test
	public void shouldBeInitialisedWithOneCopy() {
		// arrange
		Book sut = new Book();

		// act
		// assert
		Assert.assertEquals(1, sut.getNumberOfCopies());
	}

	@Test
	public void shouldBeImmutableToIsbnChanges() {
		// arrange
		Book sut = new Book("978-3-16-148410-0", UUID.randomUUID().toString(), UUID.randomUUID().toString());
		Book another = new Book("978-3-16-148411-0", sut.getTitle(), sut.getAuthor());

		// act
		// assert
		Assert.assertFalse(sut.changesAllowed(another));
	}
	
	@Test
	public void shouldAllowAddingIsbn() {
		// arrange
		Book sut = new Book("", UUID.randomUUID().toString(), UUID.randomUUID().toString());
		Book another = new Book("978-3-16-148410-0", sut.getTitle(), sut.getAuthor());

		// act
		// assert
		Assert.assertTrue(sut.changesAllowed(another));
	}

	@Test
	public void shouldBeImmutableToTitleChanges() {
		// arrange
		Book sut = new Book(UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString());
		Book another = new Book(sut.getIsbn(), UUID.randomUUID().toString(), sut.getAuthor());

		// act
		// assert
		Assert.assertFalse(sut.changesAllowed(another));
	}
}
