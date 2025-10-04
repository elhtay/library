package com.example.booklibrary.data;

import org.springframework.stereotype.Component;

import com.example.booklibrary.model.Book;
import com.example.booklibrary.repository.BookRepository;

/**
 * Initializes the repository with sample books for testing and demo.
 */
@Component
public class SampleDataInitializer implements DataInitializer {
    
    @Override
    public void initializeData(BookRepository repository) {
        repository.save(new Book("1984", "George Orwell", 1949));
        repository.save(new Book("The Catcher in the Rye", "J.D. Salinger", 1951));
        repository.save(new Book("The Hobbit", "J.R.R. Tolkien", 1937));
        repository.save(new Book("Frankenstein", "Mary Shelley", 1818));
        repository.save(new Book("The Lord of the Rings", "J.R.R. Tolkien", 1954));
    }
}
