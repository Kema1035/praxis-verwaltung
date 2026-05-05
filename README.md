# 🏥 Arztpraxis-Verwaltungssystem

A full-stack web application for managing a medical practice — built with Java, Spring Boot, and MySQL.

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?style=flat-square&logo=springboot)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=flat-square&logo=mysql)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5-purple?style=flat-square&logo=bootstrap)
![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)

---

## Overview

The **Arztpraxis-Verwaltungssystem** is a comprehensive practice management system that allows medical staff to manage patients, doctors, appointments, medical records, diagnoses, and prescriptions — all from a clean, responsive web interface.

---

## Features

-  **Doctor Management** — Add, edit, and delete doctors with specializations
-  **Patient Management** — Full patient profiles with insurance information
-  **Appointment Scheduling** — Weekly calendar view with double-booking prevention
-  **Medical Records** — Anamnesis, allergies, chronic conditions per patient
-  **Diagnoses** — Link diagnoses to patients, doctors, and appointments
-  **Prescriptions** — Issue and track prescriptions with automatic validity calculation
-  **PDF Export** — Export filtered appointment lists as PDF
-  **Role-based Security** — Admin and User roles with Spring Security
-  **Advanced Filtering** — Filter appointments by date, status, patient, and doctor
-  **Dashboard** — Overview statistics and appointment load per doctor

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| Framework | Spring Boot 3 |
| Security | Spring Security |
| ORM | Spring Data JPA / Hibernate |
| Database | MySQL 8 |
| Templates | Thymeleaf |
| Frontend | Bootstrap 5 |
| PDF | iText 5 |
| Build | Apache Maven |

---

##  Getting Started

### Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven

### Installation

**1. Clone the repository**
```bash
git clone https://github.com/Kema1035/praxis-verwaltung.git
cd praxis-verwaltung
```

**2. Create the database**
```sql
CREATE DATABASE beleg CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

**3. Import sample data**
```bash
mysql -u root -p beleg < beleg_dump.sql
```

**4. Configure database connection**

Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/beleg
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

**5. Build and run**
```bash
mvn clean package -DskipTests
java -jar target/beleg-0.0.1-SNAPSHOT.jar
```

**6. Open in browser**
```
http://localhost:8080
```

---

## Default Credentials

| Role | Username | Password |
|------|----------|----------|
| Administrator | `admin` | `12345` |
| User | `user` | `hochschule123` |

> **Admin** has full access including delete operations.  
> **User** can view and create records but cannot delete.

---

## Project Structure

```
src/main/java/com/example/beleg/
├── config/
│   ├── SecurityConfig.java       # Spring Security configuration
│   └── DataInitializer.java      # Sample data on first startup
├── controller/
│   ├── ArztController.java
│   ├── PatientController.java
│   ├── TerminController.java
│   ├── KalenderController.java
│   ├── PatientenakteController.java
│   ├── RezeptController.java
│   ├── PdfExportController.java
│   ├── IndexController.java
│   └── LoginController.java
├── model/
│   ├── Arzt.java
│   ├── Patient.java
│   ├── Termin.java
│   ├── Patientenakte.java
│   ├── Diagnose.java
│   └── Rezept.java
├── repository/
│   ├── ArztRepository.java
│   ├── PatientRepository.java
│   ├── TerminRepository.java
│   ├── PatientenakteRepository.java
│   ├── DiagnoseRepository.java
│   └── RezeptRepository.java
└── service/
    └── TerminService.java        # Business logic & double-booking prevention
```

---

## Screenshots

<img width="1470" height="878" alt="Bildschirmfoto 2026-05-05 um 22 30 41" src="https://github.com/user-attachments/assets/10369cb2-3087-4455-bd81-37acbb5d6680" />
<img width="1470" height="834" alt="Bildschirmfoto 2026-05-05 um 22 30 54" src="https://github.com/user-attachments/assets/dcfbaa1b-a3d8-4e5c-8933-01391c8683a0" />
<img width="1470" height="834" alt="Bildschirmfoto 2026-05-05 um 22 31 08" src="https://github.com/user-attachments/assets/9f0e142c-1dc0-47db-83b0-e500df491d8e" />
<img width="1470" height="880" alt="Bildschirmfoto 2026-05-05 um 22 31 01" src="https://github.com/user-attachments/assets/76842e70-3aab-4755-815e-2698594135b5" />
<img width="1470" height="833" alt="Bildschirmfoto 2026-05-05 um 22 31 14" src="https://github.com/user-attachments/assets/f38393d1-81ce-4580-9e34-69f2266686df" />
<img width="1470" height="834" alt="Bildschirmfoto 2026-05-05 um 22 31 20" src="https://github.com/user-attachments/assets/4f5a63c5-0051-4071-9ac3-a404cb0bd63e" />
<img width="1470" height="879" alt="Bildschirmfoto 2026-05-05 um 22 31 50" src="https://github.com/user-attachments/assets/20cff7fb-aa3d-4204-b637-264f20d63244" />
<img width="1470" height="877" alt="Bildschirmfoto 2026-05-05 um 22 32 07" src="https://github.com/user-attachments/assets/6a43c2ea-a4f1-4afe-b828-4ad15d0b65be" />


---

## Database Schema

The application uses 6 main tables:

- `arzt` — Doctors
- `patient` — Patients
- `termin` — Appointments
- `patientenakte` — Medical records (1:1 with patient)
- `diagnose` — Diagnoses
- `rezept` — Prescriptions

---

## Academic Context

This project was developed as a programming assignment (*Programmierbeleg*) at **Hochschule Mittweida** in the second semester of the Informatik program.

---
