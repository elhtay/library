package com.example.booklibrary.repository;

import java.util.List;
import java.util.Optional;

import com.example.booklibrary.model.Book;

/**
 * Repository interface for Book entities.
 */
public interface BookRepository {
    
    /**
     * Retrieves all books from the repository.
     *
     * @return List of all books
     */
    List<Book> findAll();
    
    /**
     * Finds a book by its title.
     *
     * @param title The title to search for
     * @return Optional containing the book if found, empty otherwise
     */
    Optional<Book> findByTitle(String title);
    
    /**
     * Saves a book to the repository.
     *
     * @param book The book to save
     * @return The saved book
     */
    Book save(Book book);
    
    /**
     * Returns the total number of books in the repository.
     *
     * @return The count of books
     */
    int count();
    
    /**
     * Deletes a book by its title.
     *
     * @param title The title of the book to delete
     * @return true if the book was deleted, false if not found
     */
    boolean deleteByTitle(String title);
}