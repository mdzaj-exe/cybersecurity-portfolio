# Computer Shop Inventory Management System

## Overview

The **Computer Shop** is a Spring Boot application designed to manage inventory for a computer store. It allows users to add, update, and manage inventory items efficiently using an intuitive web interface. This project demonstrates the use of modern web development practices with Spring Boot, Thymeleaf, and H2 Database.

## Features

- **Inventory Management**: Add, update, delete, and view computer inventory items.
- **User-friendly Interface**: Built with Thymeleaf for seamless user interaction.
- **Data Validation**: Ensures accurate and valid data entry with Hibernate Validator.
- **In-memory Database**: Uses H2 database for easy development and testing.
- **CSV Support**: Includes support for importing and exporting data using OpenCSV.

## Technologies Used

- **Java 17**: Latest features of the Java programming language.
- **Spring Boot 2.6.6**: Framework for building enterprise-grade applications.
- **Thymeleaf**: Server-side Java template engine for web applications.
- **H2 Database**: Lightweight, in-memory database for development and testing.
- **OpenCSV**: Library for CSV parsing and generation.
- **Docker**: Used to containerize the application for easy deployment.

## Prerequisites

Before you begin, ensure you have the following installed:

1. Java 17
2. Maven 3.6+
3. Docker
4. Git (optional, for version control)

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/your-repo/computer-shop.git
cd computer-shop

Build the Application
Use Maven to clean and package the project into a JAR file.

bash
mvn clean package

Run the Application Locally
You can run the application without Docker by using the Spring Boot plugin:

bash
mvn spring-boot:run

Access the application at http://localhost:8080.
Docker Instructions
Build the Docker Image
Ensure you have Docker installed and running. Use the following command to build the Docker image:

bash
docker build -t computershop-app .

Run the Docker Container
Start the application in a Docker container:

bash
docker run -p 8080:8080 computershop-app

Visit http://localhost:8080 to access the application.
