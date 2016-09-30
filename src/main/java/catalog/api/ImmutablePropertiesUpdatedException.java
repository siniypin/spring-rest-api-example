package catalog.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ImmutablePropertiesUpdatedException extends RuntimeException {
	public ImmutablePropertiesUpdatedException() {
		super("some of the book properties you tried to update are immutable");
	}
}
