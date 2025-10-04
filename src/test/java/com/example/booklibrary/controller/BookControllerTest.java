package com.example.booklibrary.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.booklibrary.exception.BookNotFoundException;
import com.example.booklibrary.model.Book;
import com.example.booklibrary.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unit tests for BookController.
 * Uses @WebMvcTest for focused web layer testing with MockMvc.
 */
@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService mockBookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllBooks_ShouldReturnAllBooksAsJson() throws Exception {
        // Given
        List<Book> books = Arrays.asList(
            new Book("1984", "George Orwell", 1949),
            new Book("The Hobbit", "J.R.R. Tolkien", 1937)
        );
        when(mockBookService.getAllBooks()).thenReturn(books);

        // When & Then
        mockMvc.perform(get("/books"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].title").value("1984"))
            .andExpect(jsonPath("$[0].author").value("George Orwell"))
            .andExpect(jsonPath("$[0].year").value(1949))
            .andExpect(jsonPath("$[1].title").value("The Hobbit"))
            .andExpect(jsonPath("$[1].author").value("J.R.R. Tolkien"))
            .andExpect(jsonPath("$[1].year").value(1937));

        verify(mockBookService).getAllBooks();
    }

    @Test
    void getBookByTitle_WhenBookExists_ShouldReturnBook() throws Exception {
        // Given
        String title = "1984";
        Book book = new Book("1984", "George Orwell", 1949);
        when(mockBookService.getBookByTitle(title)).thenReturn(Optional.of(book));

        // When & Then
        mockMvc.perform(get("/books/{title}", title))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.title").value("1984"))
            .andExpect(jsonPath("$.author").value("George Orwell"))
            .andExpect(jsonPath("$.year").value(1949));

        verify(mockBookService).getBookByTitle(title);
    }

    @Test
    void getBookByTitle_WhenBookDoesNotExist_ShouldReturn404() throws Exception {
        // Given
        String title = "Non-existent Book";
        when(mockBookService.getBookByTitle(title)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/books/{title}", title))
            .andExpect(status().isNotFound());

        verify(mockBookService).getBookByTitle(title);
    }

    @Test
    void addBook_WithValidBook_ShouldReturnCreatedBook() throws Exception {
        // Given
        Book inputBook = new Book("New Book", "New Author", 2023);
        Book savedBook = new Book("New Book", "New Author", 2023);
        when(mockBookService.addBook(any(Book.class))).thenReturn(savedBook);

        // When & Then
        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputBook)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.title").value("New Book"))
            .andExpect(jsonPath("$.author").value("New Author"))
            .andExpect(jsonPath("$.year").value(2023));

        verify(mockBookService).addBook(any(Book.class));
    }

    @Test
    void addBook_WithInvalidBook_ShouldReturn400() throws Exception {
        // Given
        Book invalidBook = new Book("", "Author", 2023);
        when(mockBookService.addBook(any(Book.class)))
            .thenThrow(new IllegalArgumentException("Book title is required"));

        // When & Then
        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidBook)))
            .andExpect(status().isBadRequest());

        verify(mockBookService).addBook(any(Book.class));
    }

    @Test
    void deleteBookByTitle_WhenBookExists_ShouldReturn204() throws Exception {
        // Given
        String title = "Book to Delete";
        doNothing().when(mockBookService).deleteBookByTitle(title);
        
        // When & Then
        mockMvc.perform(delete("/books/{title}", title))
            .andExpect(status().isNoContent());
        
        verify(mockBookService).deleteBookByTitle(title);
    }
    
    @Test
    void deleteBookByTitle_WhenBookDoesNotExist_ShouldReturn404WithErrorResponse() throws Exception {
        // Given
        String title = "Non-existent Book";
        doThrow(new BookNotFoundException("Book with title 'Non-existent Book' not found"))
            .when(mockBookService).deleteBookByTitle(title);
        
        // When & Then
        mockMvc.perform(delete("/books/{title}", title))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("Not Found"))
            .andExpect(jsonPath("$.message").value("Book with title 'Non-existent Book' not found"));
        
        verify(mockBookService).deleteBookByTitle(title);
    }
    
    @Test
    void deleteBookByTitle_WithInvalidTitle_ShouldReturn400WithErrorResponse() throws Exception {
        // Given
        String invalidTitle = "   ";  // whitespace-only title
        doThrow(new IllegalArgumentException("Book title is required"))
            .when(mockBookService).deleteBookByTitle(invalidTitle);
        
        // When & Then
        mockMvc.perform(delete("/books/{title}", invalidTitle))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("Bad Request"))
            .andExpect(jsonPath("$.message").value("Book title is required"));
        
        verify(mockBookService).deleteBookByTitle(invalidTitle);
    }
}