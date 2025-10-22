# 📚 Book Subscription & Notification System

 A modern Spring Boot application enabling users to register, confirm their emails, manage books, and receive automated daily notifications about new releases from their subscribed authors or genres.

# 🌟 Overview

The Book Subscription & Notification System is a scalable and secure platform that allows users to:

Register and verify their accounts via email confirmation.

Add and manage books in the system.

Subscribe to specific authors or genres.

Receive consolidated email notifications (once per 24 hours) when new books from their interests are added.

This design ensures efficient email batching, avoids spam, and enhances the user experience through reliable event-driven notifications.

# ✨ Key Features

**User Registration & Email Verification** -
Secure user onboarding with email-based account activation using Spring Boot Mail.

**Book Management** -
Users can add, update, and list books with ease via REST APIs backed by JPA.

**Subscription System** -
Subscribe to authors or genres and stay updated automatically.

**Smart Email Notifications** -
Users receive one email per day summarizing all relevant new books from their subscriptions.

**Secure Authentication & Authorization** -
Robust endpoint protection and role-based access control using Spring Security.

**Scheduled Notifications** -
Automated daily email dispatch via Spring scheduling.

# 🛠️ Tech Stack

- **Java**
- **Spring Boot**: 3.1.4
- **Spring Security**: 6.1.4	
- **Spring Data JPA**:	Database operations
- **Spring Boot Mail**:	Email service integration
- **Spring Boot Validation / Hibernate Validator**: 7.0.2.Final	Data validation
- **H2 Database**:	In-memory database for testing and development
- **Lombok**:	Boilerplate reduction
- **JUnit / Spring Boot Test / Spring Security Test**	Testing framework
# 🧱 Architecture

- **Controller Layer** – Exposes RESTful endpoints for user and book operations.

- **Service Layer** – Encapsulates business logic including notification scheduling and email batching.

- **Repository Layer** – Manages persistence using Spring Data JPA.

- **Scheduler** – Runs once every 24 hours to aggregate and send pending notifications.

- **Mail Service** – Handles email delivery via SMTP.
