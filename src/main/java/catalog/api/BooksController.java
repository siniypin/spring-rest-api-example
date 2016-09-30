package catalog.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BooksController {
	@RequestMapping("/catalog/books")
	public String[] books() {
		return new String[] {"ABC", "second", "blue"};
	}
}