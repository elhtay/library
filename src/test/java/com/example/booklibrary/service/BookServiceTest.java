package com.example.booklibrary.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.booklibrary.exception.BookNotFoundException;
import com.example.booklibrary.model.Book;
import com.example.booklibrary.repository.BookRepository;

/**
 * Unit tests for BookService.
 * Uses Mockito to isolate the service layer from repository dependencies.
 */
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository mockRepository;
    
    private BookService bookService;
    
    @BeforeEach
    void setUp() {
        bookService = new BookService(mockRepository);
    }
    
    @Test
    void getAllBooks_ShouldReturnAllBooksFromRepository() {
        // Given
        List<Book> expectedBooks = Arrays.asList(
            new Book("1984", "George Orwell", 1949),
            new Book("The Hobbit", "J.R.R. Tolkien", 1937)
        );
        when(mockRepository.findAll()).thenReturn(expectedBooks);
        
        // When
        List<Book> result = bookService.getAllBooks();
        
        // Then
        assertEquals(expectedBooks, result);
        verify(mockRepository).findAll();
    }
    
    @Test
    void getBookByTitle_WhenBookExists_ShouldReturnBook() {
        // Given
        String title = "1984";
        Book expectedBook = new Book("1984", "George Orwell", 1949);
        when(mockRepository.findByTitle(title)).thenReturn(Optional.of(expectedBook));
        
        // When
        Optional<Book> result = bookService.getBookByTitle(title);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals(expectedBook, result.get());
        verify(mockRepository).findByTitle(title);
    }
    
    @Test
    void getBookByTitle_WhenBookDoesNotExist_ShouldReturnEmpty() {
        // Given
        String title = "Non-existent Book";
        when(mockRepository.findByTitle(title)).thenReturn(Optional.empty());
        
        // When
        Optional<Book> result = bookService.getBookByTitle(title);
        
        // Then
        assertFalse(result.isPresent());
        verify(mockRepository).findByTitle(title);
    }
    
    @Test
    void addBook_WithValidBook_ShouldSaveAndReturnBook() {
        // Given
        Book bookToAdd = new Book("New Book", "New Author", 2023);
        when(mockRepository.save(bookToAdd)).thenReturn(bookToAdd);
        
        // When
        Book result = bookService.addBook(bookToAdd);
        
        // Then
        assertEquals(bookToAdd, result);
        verify(mockRepository).save(bookToAdd);
    }
    
    @Test
    void addBook_WithNullBook_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> bookService.addBook(null)
        );
        
        assertEquals("Book cannot be null", exception.getMessage());
        verifyNoInteractions(mockRepository);
    }
    
    @Test
    void addBook_WithNullTitle_ShouldThrowException() {
        // Given
        Book invalidBook = new Book(null, "Author", 2023);
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> bookService.addBook(invalidBook)
        );
        
        assertEquals("Book title is required", exception.getMessage());
        verifyNoInteractions(mockRepository);
    }
    
    @Test
    void addBook_WithEmptyTitle_ShouldThrowException() {
        // Given
        Book invalidBook = new Book("   ", "Author", 2023);
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> bookService.addBook(invalidBook)
        );
        
        assertEquals("Book title is required", exception.getMessage());
        verifyNoInteractions(mockRepository);
    }
    
    @Test
    void addBook_WithNullAuthor_ShouldThrowException() {
        // Given
        Book invalidBook = new Book("Title", null, 2023);
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> bookService.addBook(invalidBook)
        );
        
        assertEquals("Book author is required", exception.getMessage());
        verifyNoInteractions(mockRepository);
    }
    
    @Test
    void addBook_WithInvalidYear_ShouldThrowException() {
        // Given
        Book invalidBook = new Book("Title", "Author", -1);
        
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> bookService.addBook(invalidBook)
        );
        
        assertEquals("Book year must be positive", exception.getMessage());
        verifyNoInteractions(mockRepository);
    }
    
    @Test
    void getBookCount_ShouldReturnCountFromRepository() {
        // Given
        int expectedCount = 5;
        when(mockRepository.count()).thenReturn(expectedCount);
        
        // When
        int result = bookService.getBookCount();
        
        // Then
        assertEquals(expectedCount, result);
        verify(mockRepository).count();
    }

    @Test
    void deleteBookByTitle_WhenBookExists_ShouldDeleteSuccessfully() {
        // Given
        String title = "Book to Delete";
        when(mockRepository.deleteByTitle(title)).thenReturn(true);
        
        // When & Then
        assertDoesNotThrow(() -> bookService.deleteBookByTitle(title));
        verify(mockRepository).deleteByTitle(title);
    }
    
    @Test
    void deleteBookByTitle_WhenBookDoesNotExist_ShouldThrowBookNotFoundException() {
        // Given
        String title = "Non-existent Book";
        when(mockRepository.deleteByTitle(title)).thenReturn(false);
        
        // When & Then
        BookNotFoundException exception = assertThrows(
            BookNotFoundException.class,
            () -> bookService.deleteBookByTitle(title)
        );
        
        assertEquals("Book with title 'Non-existent Book' not found", exception.getMessage());
        verify(mockRepository).deleteByTitle(title);
    }
    
    @Test
    void deleteBookByTitle_WithNullTitle_ShouldThrowIllegalArgumentException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> bookService.deleteBookByTitle(null)
        );
        
        assertEquals("Book title is required", exception.getMessage());
        verifyNoInteractions(mockRepository);
    }
    
    @Test
    void deleteBookByTitle_WithEmptyTitle_ShouldThrowIllegalArgumentException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> bookService.deleteBookByTitle("   ")
        );
        
        assertEquals("Book title is required", exception.getMessage());
        verifyNoInteractions(mockRepository);
    }
}