package com.example.booklibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application class.
 * Entry point for the Book Library REST API.
 */
@SpringBootApplication
public class BookLibraryApplication {

    public static void main(String[] args) {
            SpringApplication.run(BookLibraryApplication.class, args);
    }
}