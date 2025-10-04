package com.example.booklibrary.repository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.booklibrary.model.Book;

@DisplayName("BookRepository Tests")
@ExtendWith(MockitoExtension.class)
class BookRepositoryTest {

    private BookRepository repository;
    private Book testBook;

    @BeforeEach
    void setUp() {
        repository = new InMemoryBookRepository(null);
        testBook = new Book("Clean Code", "Robert C. Martin", 2008);
    }

    @Test
    @DisplayName("Should save book")
    void shouldSaveBook() {
        Book saved = repository.save(testBook);
        assertNotNull(saved);
        assertEquals("Clean Code", saved.getTitle());
        assertEquals("Robert C. Martin", saved.getAuthor());
        assertEquals(2008, saved.getYear());
    }

    @Test
    @DisplayName("Should find book by title")
    void shouldFindBookByTitle() {
        repository.save(testBook);
        Optional<Book> found = repository.findByTitle("Clean Code");
        
        assertTrue(found.isPresent());
        assertEquals("Clean Code", found.get().getTitle());
        assertEquals("Robert C. Martin", found.get().getAuthor());
    }

    @Test
    @DisplayName("Should return empty when book not found")
    void shouldReturnEmptyWhenBookNotFound() {
        Optional<Book> found = repository.findByTitle("Nonexistent Book");
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Should find all books")
    void shouldFindAllBooks() {
        repository.save(testBook);
        repository.save(new Book("Effective Java", "Joshua Bloch", 2018));
        
        List<Book> books = repository.findAll();
        assertEquals(2, books.size());
    }

    @Test
    @DisplayName("Should return empty list when no books exist")
    void shouldReturnEmptyListWhenNoBooksExist() {
        List<Book> books = repository.findAll();
        assertTrue(books.isEmpty());
    }

    @Test
    @DisplayName("Should update existing book")
    void shouldUpdateExistingBook() {
        repository.save(testBook);
        
        Book updatedBook = new Book("Clean Code", "Uncle Bob", 2008);
        Book updated = repository.save(updatedBook);
        
        assertEquals("Clean Code", updated.getTitle());
        assertEquals("Uncle Bob", updated.getAuthor());
        
        Optional<Book> found = repository.findByTitle("Clean Code");
        assertTrue(found.isPresent());
        assertEquals("Uncle Bob", found.get().getAuthor());
    }

    @Test
    @DisplayName("Should delete book by title")
    void shouldDeleteBookByTitle() {
        repository.save(testBook);
        
        boolean deleted = repository.deleteByTitle("Clean Code");
        assertTrue(deleted);
        
        Optional<Book> found = repository.findByTitle("Clean Code");
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Should return false when deleting non-existent book")
    void shouldReturnFalseWhenDeletingNonExistentBook() {
        boolean deleted = repository.deleteByTitle("Nonexistent Book");
        assertFalse(deleted);
    }

    @Test
    @DisplayName("Should count books correctly")
    void shouldCountBooksCorrectly() {
        assertEquals(0, repository.count());
        
        repository.save(testBook);
        assertEquals(1, repository.count());
        
        repository.save(new Book("Effective Java", "Joshua Bloch", 2018));
        assertEquals(2, repository.count());
        
        repository.deleteByTitle("Clean Code");
        assertEquals(1, repository.count());
    }

    @Test
    @DisplayName("Should handle null book in save")
    void shouldHandleNullBookInSave() {
        assertThrows(IllegalArgumentException.class, () -> repository.save(null));
    }

    @Test
    @DisplayName("Should handle book with null title")
    void shouldHandleBookWithNullTitle() {
        Book bookWithNullTitle = new Book();
        bookWithNullTitle.setAuthor("Some Author");
        bookWithNullTitle.setYear(2020);
        
        assertThrows(IllegalArgumentException.class, () -> repository.save(bookWithNullTitle));
    }
}
