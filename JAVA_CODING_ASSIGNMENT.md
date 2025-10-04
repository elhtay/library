# Book Library Java Assignment

This assignment tests basic Java skills, understanding of classes, and ability to expose a simple HTTP API using Spring Boot.

## Project Goal

Create a small in-memory book library that can be accessed via HTTP endpoints. All data must be returned as JSON.

## Requirements

1. Implement a `Book` class with the following fields:
   - `String title`
   - `String author`
   - `int year`
   - Include constructor, getters, and setters.

2. Implement a **repository/service** that holds an in-memory list of books. Prepopulate it with at least 3 sample books.

3. Implement a **REST controller** with the following endpoints:
   - `GET /books` → returns **all books sorted by title** in JSON.
   - `GET /books/{title}` → returns **a single book by title** in JSON (or 404 if not found).

4. Implement **unit tests** for the repository/service and controller to ensure correctness of the main functionality.

5. Use **Spring Boot** to run the HTTP server.

## Suggested Project Structure

```text
book-library/
│
├─ src/
│  ├─ main/
│  │  ├─ java/com/example/booklibrary/
│  │  │  ├─ Book.java                  # Provided class
│  │  │  ├─ (candidate creates repository/service here)
│  │  │  ├─ (candidate creates controller here)
│  │  │  └─ BookLibraryApplication.java # Main Spring Boot application
│  │  └─ resources/
│  │     └─ application.properties
├─ src/
│  └─ test/
│     └─ java/com/example/booklibrary/
│        └─ (candidate writes unit tests here)
└─ pom.xml
