# 📦 Parcel Tracking Microservice Application

A microservice-based application that allows users to place parcel orders and receive email notifications. Built with Spring Boot, Angular, Kafka, and MySQL, this system ensures smooth, asynchronous communication and reliable delivery updates.

---

## 🏗️ Architecture Overview

This project includes two key microservices:

- **📦 Order Service**: Processes parcel order data and publishes events to Kafka.
- **📧 Notification Service**: Listens to Kafka topics, formats parcel information, and sends emails to customers.

---

## 🚀 Tech Stack

![Java](https://img.shields.io/badge/Java%2021-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)  
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)  
![Angular](https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white)  
![Apache Kafka](https://img.shields.io/badge/Kafka-231F20?style=for-the-badge&logo=apache-kafka&logoColor=white)  
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)  
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)


---

## 🛠️ Service Details

### 📦 Order Service

- Accepts parcel details like size, weight, destination address, and contact info.
- Generates a unique AWB number.
- Sends parcel data to a Kafka topic.
- Persists parcel records in a MySQL database.

### 📧 Notification Service

- Subscribes to Kafka topic to receive new parcel events.
- Formats parcel information.
- Sends a confirmation email to the customer using Spring Mail.

---

## 🔄 Communication Flow

1. Customer submits a parcel order through the Angular frontend.
2. Order Service receives the request, stores the data, and publishes it to Kafka.
3. Notification Service consumes the Kafka message.
4. An email is generated and sent to the customer with parcel details.

---

## ▶️ Running the Application

### ✅ Prerequisites

- Docker & Docker Compose  
- Java 21  
- Maven  
- Node.js & Angular CLI  
- Kafka & Zookeeper (can be started via Docker)

### 📑 API Documentation
Once the Order Service is running, access Swagger UI at:
👉 http://localhost:8080/swagger-ui.html



### 📦 Backend Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/PopescuAdrianIulian/SdaFinalProject
   cd SdaFinalProject
   ```

2. Start Kafka and Zookeeper using Docker Compose:
   ```bash
   docker-compose up -d
   ```

3. Run the Order Service:
   ```bash
   cd orderservice
   mvn spring-boot:run
   ```

4. Run the Notification Service:
   ```bash
   cd notificationservice
   mvn spring-boot:run
   ```

### 🖥️ Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies and run the Angular app:
   ```bash
   npm install
   ng serve
   ```
