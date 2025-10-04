package com.example.booklibrary.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.booklibrary.exception.BookNotFoundException;
import com.example.booklibrary.model.Book;
import com.example.booklibrary.repository.BookRepository;

/**
 * Service layer for book operations.
 * Handles logic and coordinates between controller and repository.
 */
@Service
public class BookService {
    
    private final BookRepository bookRepository;
    
    /**
     * Constructor injection for better testability and immutability
     */
    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    
    /**
     * Retrieves all books sorted by title.
     *
     * @return List of all books sorted alphabetically by title
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    /**
     * Finds a book by its title (case-insensitive).
     *
     * @param title The title to search for
     * @return Optional containing the book if found, empty otherwise
     */
    public Optional<Book> getBookByTitle(String title) {
        return bookRepository.findByTitle(title);
    }
    
    /**
     * Adds a new book to the library.
     *
     * @param book The book to add
     * @return The saved book
     * @throws IllegalArgumentException if the book is invalid
     */
    public Book addBook(Book book) {
        validateBook(book);
        return bookRepository.save(book);
    }
    
    /**
     * Gets the total count of books in the library.
     *
     * @return The number of books
     */
    public int getBookCount() {
        return bookRepository.count();
    }
    
    /**
     * Deletes a book by its title.
     *
     * @param title The title of the book to delete
     * @throws BookNotFoundException if the book is not found
     * @throws IllegalArgumentException if the title is invalid
     */
    public void deleteBookByTitle(String title) {
        validateTitle(title);
        boolean deleted = bookRepository.deleteByTitle(title);
        if (!deleted) {
            throw new BookNotFoundException("Book with title '" + title + "' not found");
        }
    }
    
    /**
     * Validates a title string.
     *
     * @param title The title to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Book title is required");
        }
    }
    
    /**
     * Help method
     * Validates a book object for required fields.
     *
     * @param book The book to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Book title is required");
        }
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("Book author is required");
        }
        if (book.getYear() <= 0) {
            throw new IllegalArgumentException("Book year must be positive");
        }
    }
}