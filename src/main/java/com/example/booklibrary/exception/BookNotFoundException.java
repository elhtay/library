package com.example.booklibrary.exception;

/**
 * Exception raised when the library cannot find the requested book.
 */
public class BookNotFoundException extends RuntimeException {
    
    /**
     * Constructs a new BookNotFoundException with the specified detail message.
     *
     * @param message the detail message explaining why the book was not found
     */
    public BookNotFoundException(String message) {
        super(message);
    }
}