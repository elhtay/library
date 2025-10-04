# Book Library REST API

A Spring Boot application that provides a simple in-memory book library with REST endpoints.

## Architecture Overview

```
┌─────────────────┐
│   Controller    │ ← REST endpoints, HTTP handling
│   (Web Layer)   │
└─────────────────┘
         │
         ▼
┌─────────────────┐
│    Service      │ ← Logic, validation
│ (Business Layer)│
└─────────────────┘
         │
         ▼
┌─────────────────┐
│   Repository    │ ← Data access abstraction
│  (Data Layer)   │
└─────────────────┘
         │
         ▼
┌─────────────────┐
│   In-Memory     │ ← Concrete data storage
│   Storage       │
└─────────────────┘
```

## Project Structure

```
book-library/
├── src/
│   ├── main/
│   │   ├── java/com/example/booklibrary/
│   │   │   ├── BookLibraryApplication.java         # Main Spring Boot application
│   │   │   ├── controller/
│   │   │   │   └── BookController.java             # REST controller
│   │   │   ├── data/
│   │   │   │   ├── DataInitializer.java            # Data initialization interface
│   │   │   │   └── SampleDataInitializer.java      # Sample data implementation
│   │   │   ├── exception/
│   │   │   │   ├── BookNotFoundException.java      # Custom exception
│   │   │   │   └── ErrorResponse.java              # Structured error response
│   │   │   ├── model/
│   │   │   │   └── Book.java                       # Entity class
│   │   │   ├── repository/
│   │   │   │   ├── BookRepository.java             # Repository interface
│   │   │   │   └── InMemoryBookRepository.java     # In-memory implementation
│   │   │   └── service/
│   │   │       └── BookService.java                # Service layer
│   │   └── resources/
│   │       └── application.properties               # Configuration
│   └── test/
│       └── java/com/example/booklibrary/
│           ├── controller/
│           │   └── BookControllerTest.java          
│           ├── repository/
│           │   └── BookRepositoryTest.java          
│           └── service/
│               └── BookServiceTest.java             
└── pom.xml                                          
```


## API Endpoints

### GET /books
Returns all books sorted alphabetically by title.

**Response Example:**
```json
[
  {
    "title": "1984",
    "author": "George Orwell",
    "year": 1949
  },
  {
    "title": "Frankenstein",
    "author": "Mary Shelley",
    "year": 1818
  }
]
```

### GET /books/{title}
Returns a single book by title (case-insensitive search).

**Example:** `GET /books/1984`

**Response Example:**
```json
{
  "title": "1984",
  "author": "George Orwell",
  "year": 1949
}
```

**404 Response:** If book not found, returns HTTP 404 status with structured error response:
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Book with title '1984' not found",
  "path": "/books/1984"
}
```

### POST /books
Adds a new book to the library.

**Request Body Example:**
```json
{
  "title": "New Book",
  "author": "New Author",
  "year": 2023
}
```

**Response:** Returns HTTP 201 Created with the created book details.

### DELETE /books/{title}
Deletes a book from the library by title.

**Example:** `DELETE /books/1984`

**Response:** Returns HTTP 204 No Content on successful deletion.

**404 Response:** If book not found, returns HTTP 404 status with structured error response.

## How to Run

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

1. **Compile and Package:**
   ```bash
   mvn clean package
   ```

2. **Run the Application:**
   ```bash
   mvn spring-boot:run
   ```

   Or alternatively:
   ```bash
   java -jar target/book-library-1.0.0.jar
   ```

3. **The application will start on port 8080**

### Running Tests

```bash
mvn test
```

## Testing the API

Once the application is running, you can test the endpoints:

### Get all books:
```bash
curl -X GET http://localhost:8080/books
```

### Get a specific book:
```bash
curl -X GET http://localhost:8080/books/1984
```

### Add a new book:
```bash
curl -X POST http://localhost:8080/books \
  -H "Content-Type: application/json" \
  -d '{"title":"Test Book","author":"Test Author","year":2023}'
```

### Delete a book:
```bash
curl -X DELETE http://localhost:8080/books/1984
```
