package catalog;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import catalog.models.BooksRepository;
import catalog.models.DefaultInMemoryBooksRepositoryImpl;

@Configuration
public class EndpointConfig {
	@Bean
	public BooksRepository booksRepository(){
		return new DefaultInMemoryBooksRepositoryImpl();
	}
}
