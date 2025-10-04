package com.example.booklibrary.model;

import java.util.Objects;

/**
 * Represents a book in the library system.
 */
public class Book {
    private String title;
    private String author;
    private int year;

    /**
     * Default constructor required for JSON serialization/deserialization
     */
    public Book() {
    }

    /**
     * Constructor with all required fields
     *
     * @param title  The book title (cannot be null or empty)
     * @param author The book author (cannot be null or empty)
     * @param year   The publication year
     */
    public Book(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return year == book.year &&
               Objects.equals(title, book.title) &&
               Objects.equals(author, book.author);
    }

    // Needed for correct behavior in hash-based collections like HashMap or HashSet.
    @Override
    public int hashCode() {
        return Objects.hash(title, author, year);
    }

    @Override
    public String toString() {
        return "Book{" +
               "title='" + title + '\'' +
               ", author='" + author + '\'' +
               ", year=" + year +
               '}';
    }
}