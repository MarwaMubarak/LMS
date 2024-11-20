# Library Management System (LMS) Backend

This project implements a mini backend system for a Library Management System (LMS) with functionalities such as user management, book management, borrowing history, and basic scripting tasks. The system uses **Spring Boot** for the backend API, **PostgreSQL** for the database, and **Python** for data generation and operations.

## Thought Process Behind the Design and Implementation

### **Architecture and Design**

The backend is built following the **Model-View-Controller (MVC)** pattern, where:

- **Model**: Represents the core components (users, books, borrowing history).
- **View**: Handled by the REST API responses.
- **Controller**: Manages the API requests and routes, with services to handle business logic.

### **Part 1: Database Design**

The PostgreSQL schema is optimized for performance, with inheritance and indexing for fast data retrieval:

1. **Users Table**: Includes `id`, `name`, `email`, `password`, and `role` (either **ADMIN** or **PATRON**). Users with the **ADMIN** role can add books and manage the system, while **PATRON** users can borrow books.
2. **Staff Table**: Inherits from the `Users` table, adding `position` and `salary` attributes for staff members.
3. **Books Table**: Contains book information such as `title`, `author`, `isbn`, and `copies_available`. Indexes are created for `title` and `author` to optimize search queries.
4. **BorrowingHistory Table**: Stores the borrowing history of users, linking `user_id` and `book_id`, with `borrow_date` and `return_date`. Indexes optimize lookups by `user_id` and `book_id`.

**Indexes**:
- Added on `title` and `author` in the `Books` table for efficient searching.
- Added on `user_id` and `book_id` in the `BorrowingHistory` table to speed up query performance when retrieving user borrowing history.

### **Part 2: API Development**

The API is built using **Spring Boot** with the following endpoints:

1. **POST /users/register**: Allows the creation of new users (both Admins and Patrons). Admins can add new Patrons, while Patrons cannot add users.
2. **GET /books**: Fetches books by title or author. This endpoint supports **query parameters** to search books.
3. **POST /borrow**: Records borrowing events, reducing the `copies_available` for the borrowed book and storing the transaction in the `BorrowingHistory` table.
4. **GET /borrow/history/{userId}**: Retrieves the borrowing history for a specific user.
5. **POST /login**: Authenticates users based on their email and password.

**Validation**:
- **Admins** can add new books.
- Prevents borrowing if `copies_available` is zero.
- Ensures all fields (e.g., `email`, `password`, `name`) are validated before creating a user or adding a book.

### **Part 3: Python Scripting**

Python is used to handle tasks such as data generation and exporting unused books:

1. **Data Generation**:
   - Generates **100 books**, **50 users**, and **200 random borrowing histories** using the **Faker** library.
   - Each borrowing history entry associates a user with a book, a `borrow_date`, and a `return_date`.

2. **Export Books Not Borrowed in Last 6 Months**:
   - The script checks which books have not been borrowed in the past 6 months.
   - Exports this list to a **CSV file** using **pandas**.

3. **CSV Export**: Exports generated records for books, users, and borrowing histories to CSV files for further analysis or data usage.

### **Part 4: Deployment and Containerization**

- **Docker** is used to containerize the application:
  - A **PostgreSQL container** is created to host the database.
  - The **Spring Boot application** is containerized and linked to the database container.
  - **Docker Compose** is used to set up the multi-container environment, allowing the backend and database to run together.

---
## Project Structure
```
LMS (GitHub)
├── lms
│   ├── src
│   │   ├── main
│   │   │   ├── java/com/lms
│   │   │   │   ├── config         # Configuration files for the application
│   │   │   │   ├── controller     # REST controllers for API endpoints
│   │   │   │   ├── dto            # Data Transfer Objects
│   │   │   │   ├── mapper         # Mapping logic between entities and DTOs
│   │   │   │   ├── model          # Entity models for database tables
│   │   │   │   ├── repository     # JPA repositories for data access
│   │   │   │   ├── security       # Security configurations (e.g., authentication)
│   │   │   │   ├── service        # Business logic services
│   │   │   │   ├── utility        # Helper utilities
│   │   │   │   └── LmsApplication.java # Main Spring Boot application
│   │   ├── resources
│   │   │   └── application.yml    # Configuration file
│   ├── test                       # Test files for the application
│   ├── dockerfile                 # Dockerfile for containerization
│   ├── compose.yaml               # Docker Compose configuration
│   ├── pom.xml                    # Maven project descriptor
│   ├── target
│   │   └── lms-0.0.1-SNAPSHOT.jar # Compiled Spring Boot application JAR
├── lms-py/                        # Auxiliary Python project for additional functionality
│   ├── lms.py                     # Python script related to the LMS
│   ├── books.csv                  # Sample data for books
│   ├── borrowing_histories.csv    # Sample data for borrowing histories
│   ├── not_borrowed_last_6_months.csv # Data for books not borrowed in the last six months
│   ├── users.csv                  # Sample data for users
├── env.yml                        # Environment-specific configuration
├── Instructions to set up and run the project.pdf # Setup guide
├── README.md                      # Project documentation
├── Schema.pdf                     # Database schema
├── APIs_Documentation.pdf         # API documentation
   
 
```
---

## API Endpoints

### **Books**

1. **GET /books** - Search for books by title or author.
   - **Example**: `GET /books?title=koko`
   
2. **POST /books** - Add a new book (only for Admins).
   - **Request Body**:
     ```json
     {
         "title": "Koko",
         "author": "Marwa",
         "isbn": "1234567891014",
         "copiesAvailable": 20
     }
     ```

### **Borrowing History**

1. **POST /borrow** - Borrow a book.
   - **Request Body**:
     ```json
     {
         "bookId": 3203,
         "borrowDate": "2020-05-21",
         "returnDate": "2020-10-21"
     }
     ```

2. **GET /borrow/history/{userId}** - Get the borrowing history for a specific user.

### **Users**

1. **POST /users/register** - Register a new user (Admin or Patron).
   - **Request Body**:
     ```json
     {
         "email": "marwa4@gmail.com",
         "password": "12345678",
         "name": "Marwa"
     }
     ```

2. **POST /login** - Login for authentication.
   - **Request Body**:
     ```json
     {
         "email": "marwa4@gmail.com",
         "password": "12345678"
     }
     ```

---


This solution demonstrates a clean, well-architected approach to building a Library Management System backend with proper database optimization, API design, and efficient data generation.
