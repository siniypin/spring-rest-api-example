package catalog.models;

import static org.junit.Assert.*;

import java.util.ArrayList;

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
}
