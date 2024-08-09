### Welcome to the Library Management System! This application is designed to efficiently manage books, patrons, and borrowing records for a library. Built using the Spring Boot framework, it leverages modern software design principles and best practices to provide a robust and user-friendly experience.

## Features
- **MVC Architecture**: The system follows the Model-View-Controller (MVC) design pattern to separate concerns and ensure a clean, maintainable codebase.
- **Validation**: Comprehensive validation is implemented to ensure data integrity and consistency. The system uses Spring Validation to enforce business rules and provide user feedback.
- **Security**: Security is a top priority, with Spring Security used to safeguard the application. Custom authentication and authorization mechanisms ensure that only authorized users can access or modify data.
- **Caching**: For improved performance, Spring Caching is utilized to optimize frequently accessed data.
- Transactional Management: The application uses Spring's transactional management to ensure data consistency and integrity during complex operations.
- **AOP (Aspect-Oriented Programming**): Aspect-Oriented Programming is employed to modularize cross-cutting concerns such as logging and error handling.
- **Error Handling**: Centralized error handling and logging mechanisms are in place to capture and manage application errors effectively.

## How to Run the Application
#### To get the Library Management System up and running on your local machine, follow these steps:

#### Prerequisites
Before you start, make sure you have the following installed on your system:

- Java Development Kit (JDK): Version 17 or later.
- Maven: for building the project.
- MySQL: for the application's database.

#### 1. Clone the repositoy:
The application.properties file has the configurations you will need to run this project including connecting to MySQL data base.
**Hint**: To specify the name of your schema, user name of data base and passowrd you only need to change these lines:
```java
spring.datasource.url=jdbc:mysql://localhost:3306/Your-schema-name
spring.datasource.username=user-name
spring.datasource.password=password
```
#### 2. Run using IDE or Maven:
The POM.xml file has all the dependencies needed to start running your progeam via IDE or build with Maven.

### API Documentation
#### Security:
> **Registration end point :  /api/librarians/register**

This app utilizes Spring security features to implement **User Password Authentication**
In order to access secured end points you will first need to be registered.
You can register a new user, "in this case a Librarian," by sending a **POST** request to the /api/librarians/register endpoint with the required user details in the request body. Once registered, the librarian can authenticate and access the secured endpoints of the application to manage books, patrons, and borrowing records.

#### End points:
1. ##### Book end points:
> Mapping: "/api/books"

| Method  | Return value |
| ------------ | ------------ |
| Get  | List of all books or empty list if non exist  |
| Get/{id}  | Book with the same id or 404 if not found  |
| POST, body : Book  | Create a new book  |
| PUT/{id of old}, body: new book  | Updates book with the old id   |
| DELETE/{id} | Deletes Book with same id |

2.  ##### Patron end points:
> Mapping: "/api/patrons"

| Method  | Return value |
| ------------ | ------------ |
| Get  | List of all patrons or empty list if non exist  |
| Get/{id}  | patrons with the same id or 404 if not found  |
| POST, body : patron  | Create a new patron  |
| PUT/{id of old patron}, body: new patron  | Updates old patron with id param |
| DELETE/{id} | Deletes patron with same id |


3. ##### Patron end points:
> Mapping: "/api/borrow/{bookId}/patron/{patronId}"

| Method  | Return value |
| ------------ | ------------ |
| POST | Allows Patron with PatronId to borrow book with BookId  |
| PUT  | Allows Patron to return Book  |

