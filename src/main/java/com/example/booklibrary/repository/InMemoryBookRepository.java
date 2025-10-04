package com.example.booklibrary.repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.booklibrary.data.DataInitializer;
import com.example.booklibrary.model.Book;

/**
 * In-memory implementation of BookRepository.
 * Uses thread-safe collections for concurrent access.
 * Filled with sample data by DataInitializer.
 */
@Repository
public class InMemoryBookRepository implements BookRepository {
    // Used instead of Lock
    private final Map<String, Book> books = new ConcurrentHashMap<>();

    /**
     * Constructor that initializes the repository with data using the provided
     * initializer
     * 
     * @param dataInitializer for initializing data (can be null to
     *                        skip initialization)
     */
    @Autowired(required = false)
    public InMemoryBookRepository(DataInitializer dataInitializer) {
        if (dataInitializer != null) {
            initializeData(dataInitializer);
        }
    }

    private void initializeData(DataInitializer dataInitializer) {
        dataInitializer.initializeData(this);
    }

    @Override
    public List<Book> findAll() {
        return books.values()
                .stream()
                .sorted(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    @Override
    public Optional<Book> findByTitle(String title) {
        // Validate that the input string is not null or empty
        if (title == null || title.trim().isEmpty()) {
            return Optional.empty();
        }

        // Case-insensitive search
        return books.values()
                .stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title.trim()))
                .findFirst();
    }

    @Override
    public Book save(Book book) {
        if (book == null || book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Book and title cannot be null or empty");
        }

        String key = book.getTitle().toLowerCase().trim();
        books.put(key, book);
        return book;
    }

    @Override
    public int count() {
        return books.size();
    }

    @Override
    public boolean deleteByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return false;
        }

        String key = title.toLowerCase().trim();
        return books.remove(key) != null;
    }
}