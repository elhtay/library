package com.example.booklibrary.data;

import com.example.booklibrary.repository.BookRepository;

/**
 * Interface for initializing data in the repository.
 * This abstraction allows for easy switching between sample data and real database initialization.
 */
public interface DataInitializer {
    
    /**
     * Initializes data in the provided repository
     * @param repository the repository to initialize with data
     */
    void initializeData(BookRepository repository);
}
