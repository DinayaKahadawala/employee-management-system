# Employee Management System (EMS)

## Overview

The **Employee Management System (EMS)** is a web-based application developed as a **6-member group project** at the **Sri Lanka Institute of Information Technology (SLIIT)**.

The system was designed for **Lanka Tech Solutions (Pvt) Ltd** to simplify employee management activities and reduce manual administrative work. It enables administrators and HR staff to manage employee information, attendance records, performance evaluations, shift schedules, notifications, and user access through a centralized platform.

---

## Objectives

* Manage employee information digitally
* Track employee attendance
* Monitor employee performance
* Manage employee shift schedules
* Send alerts and notifications
* Control user roles and access permissions
* Reduce manual HR processes

---

## Tech Stack

### Backend

* Java
* Spring Boot
* Spring Security
* Spring Data JPA

### Frontend

* Thymeleaf
* HTML
* CSS

### Database

* MySQL (configured via `application.properties`)

### Build Tool

* Maven

---

## Core Features

### Employee Profile Management

* Add employee records
* Update employee information
* View employee details
* Remove employee records

### Attendance Management

* Record employee attendance
* View attendance records
* Identify absentee employees

### Performance Monitoring

* Add performance feedback
* Track performance ratings
* Monitor employee performance trends

### Shift Management

* Create and manage shifts
* Assign employees to shifts
* Update shift schedules
* Handle shift change requests

### Notifications and Alerts

* Attendance alerts
* Performance reminders
* Missing information notifications
* Administrative notifications

### User Roles and Access Management

* Create user accounts
* Assign roles
* Reset passwords
* Control feature access using role-based permissions

---

## Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd Employee-Management-System
```

### 2. Configure the Database

Update your database credentials in:

```text
src/main/resources/application.properties
```

### 3. Build the Project

```bash
mvn clean install
```

### 4. Run the Application

```bash
mvn spring-boot:run
```

### 5. Access the Application

Open your browser and visit:

```text
http://localhost:8082
```

---

## Documentation

Project documents are available in the `documentation/` folder:

* Final Report
* Use Case Diagram
* Activity Diagrams
* Test Cases
* Requirement Gathering Report
* Project Screenshots

---

## Project Epics

| Epic                          | Description                                                        |
| ----------------------------- | ------------------------------------------------------------------ |
| Manage Employee Profiles      | Add, edit, view, and remove employee records                       |
| Track Attendance              | Daily attendance recording and reporting                           |
| Monitor Performance           | Performance feedback, ratings, and monitoring                      |
| Manage Employee Shifts        | Shift creation, assignment, and shift change management            |
| Send Alerts and Notifications | Attendance alerts, reminders, and system notifications             |
| Manage User Roles and Access  | User accounts, role assignment, password reset, and access control |

---

## Team

This project was developed by a **group of 6 members**.

---

## Academic Information

* **Module:** Software Engineering (SE2030)
* **Program:** B.Sc. (Hons) in Information Technology
* **Institution:** Sri Lanka Institute of Information Technology (SLIIT)
* **Academic Year:** 2025

---
