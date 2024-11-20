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
   - Authentication: Bearer token required.
   
2. **POST /books** - Add a new book (only for Admins).
   - Authentication: Bearer token required.
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
   -Authentication: Bearer token required.
   - **Request Body**:
     ```json
     {
         "bookId": 3203,
         "borrowDate": "2020-05-21",
         "returnDate": "2020-10-21"
     }
     ```

2. **GET /borrow/history/{userId}** - Get the borrowing history for a specific user.
   - Authentication: Bearer token required.
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

Here's the guide in a more straightforward format:

---

## Project Setup and Execution Guide

### 1. Build the JAR File

1. **Navigate to the Root Directory of the Project**  
   Open a terminal or command prompt and navigate to the root folder of spring boot project lms where the `pom.xml` file is located.

2. **Run the Maven Command to Build the JAR**  
   In the root directory of the project, run the following command to clean, install, and skip tests during the build process:

   ```
   mvn clean install -DskipTests=true
   ```

   This will create a `.jar` file with the name of the project.

---

### 2. Build the Docker Image

1. **Navigate to the Folder Containing the Dockerfile**  
   Make sure you're in the root directory of lms project where the `Dockerfile` is located.

2. **Build the Docker Image**  
   Run the following Docker command to build the image:

   ```
   docker build -t lms-image:latest .
   ```

---

### 3. Run PostgreSQL & Spring Boot Containers

1. **Navigate to the Folder Containing `env.yml`**  
   Open a terminal where the `env.yml` (Docker Compose configuration file) is located.

2. **Run Docker Compose to Start Containers**  
   Execute the following command to start PostgreSQL and Spring Boot containers in detached mode:

   ```
   docker-compose -f env.yml up -d
   ```

   This will start the containers based on the configuration in `env.yml` and run them in the background.

---

### 4. Send the Register and Login Requests

1. **Send a Register Request**  
   Use Postman or another API client to send a POST request to the registration endpoint:

   - **POST URL**: `http://localhost:8080/users/register`
   - **Body (raw JSON)**:

     ```json
     {
       "email": "marwa4@gmail.com",
       "password": "12345678",
       "name": "marwa"
     }
     ```

2. **Send a Login Request**  
   After registration, send a POST request to the login endpoint to get a JWT token:

   - **POST URL**: `http://localhost:8080/login`
   - **Body (raw JSON)**:

     ```json
     {
       "email": "marwa4@gmail.com",
       "password": "12345678"
     }
     ```

3. **Retrieve the Token**  
   The login response will contain a JWT token. Copy this token as you will need it in the next steps.

---

### 5. Install Python Libraries

1. **Install the Required Libraries**  
   Run the following command to install the necessary Python libraries:

   ```
   pip install requests faker pandas
   ```

---

### 6. Run the Python Script

1. **Modify the Python Script**  
   Insert the token you received from the login response into the Python script. Replace `your_jwt_token` with the actual token:

   ```python
   headers = {
       "Authorization": "Bearer your_jwt_token",
       "Content-Type": "application/json"
   }
   ```

2. **Run the Python Script**  
   Once you've modified the script, run it to interact with the API and perform the necessary tasks.

--- 

This is the step-by-step guide to setting up and executing the project.
---


This solution demonstrates a clean, well-architected approach to building a Library Management System backend with proper database optimization, API design, and efficient data generation.
