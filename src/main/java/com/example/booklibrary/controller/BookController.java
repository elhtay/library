package com.example.booklibrary.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.example.booklibrary.exception.BookNotFoundException;
import com.example.booklibrary.exception.ErrorResponse;
import com.example.booklibrary.model.Book;
import com.example.booklibrary.service.BookService;

/**
 * REST Controller for book library operations.
 * Handles HTTP requests and returns JSON responses.
 */
@RestController
@RequestMapping("/books")
public class BookController {
    
    private final BookService bookService;
    
    /**
     * Constructor injection for better testability
     */
    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    
    /**
     * GET /books - Returns all books sorted by title
     *
     * @return ResponseEntity with list of books and HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }
    
    /**
     * GET /books/{title} - Returns a single book by title
     *
     * @param title The title of the book to find
     * @return ResponseEntity with the book if found
     * @throws BookNotFoundException if the book is not found
     */
    @GetMapping("/{title}")
    public ResponseEntity<Book> getBookByTitle(@PathVariable String title) {
        Optional<Book> book = bookService.getBookByTitle(title);
        
        if (book.isPresent()) {
            return ResponseEntity.ok(book.get());
        } else {
            throw new BookNotFoundException("Book with title '" + title + "' not found");
        }
    }
    
    // Optional endpoints for future development
    /**
     * POST /books - Adds a new book
     * The book details are provided in the request body as a JSON object.
     * 
     * @param book The book object deserialized from the request body.
     * @return ResponseEntity with the created book and HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        // The exception handler deals with validation errors
        Book savedBook = bookService.addBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }
    
    /**
     * DELETE /books/{title} - Deletes a book by title
     * 
     * @param title The title of the book to delete
     * @return ResponseEntity with HTTP 204 No Content if deleted
     * 
     * @PathVariable Extract the title from the URL path.
     */
    @DeleteMapping("/{title}")
    public ResponseEntity<Void> deleteBookByTitle(@PathVariable String title) {
        bookService.deleteBookByTitle(title);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Exception handler for IllegalArgumentException
     * Returns structured error response with HTTP 400 Bad Request for invalid input
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            e.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
    /**
     * Exception handler for BookNotFoundException
     * Returns structured error response with HTTP 404 Not Found
     */
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookNotFoundException(
            BookNotFoundException e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Not Found",
            e.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}