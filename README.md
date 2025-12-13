#  Smart Home Dashboard

A **backend-driven Smart Home Dashboard application** built using **Java, Spring Boot, and MySQL**, enabling real-time monitoring and control of smart devices.
The system is designed with scalability, modularity, and clean API architecture in mind.

---

##  Project Overview

The Smart Home Dashboard allows users to **monitor device status, control smart appliances, and manage user roles** through RESTful APIs.
This project focuses on **robust backend engineering**, database design, and seamless integration with frontend components.

The application follows standard **software development life cycle (SDLC)** practices and a layered backend architecture.

---

## ✨ Key Features

* 🔌 **Smart Device Management**

  * Add, update, monitor, and control smart devices
* 🔄 **Real-Time State Handling**

  * Track device states and control history
* 👤 **User Role Management**

  * Role-based access for administrators and users
* 🌐 **RESTful API Design**

  * Clean and scalable APIs built with Spring Boot
* 🗄️ **Optimized Database Design**

  * MySQL schemas with high data integrity
* 🧩 **Modular Architecture**

  * Controller–Service–Repository layering



## 🛠️ Tech Stack

### 🔹 Backend

* Java
* Spring Boot
* Spring MVC
* REST APIs

### 🔹 Database

* MySQL

### 🔹 Tools & Practices

* Git & GitHub
* SDLC
* API testing (Postman)



## ⚙️ How to Run the Project

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/your-username/smart-home-dashboard.git
cd smart-home-dashboard
```

### 2️⃣ Configure Database

* Create a MySQL database
* Update credentials in `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/smart_home
spring.datasource.username=root
spring.datasource.password=your_password
```

### 3️⃣ Run the Application

```bash
mvn spring-boot:run
```

