# NoticeManagementService

## Project Overview

The NoticeManagementService contain 3 part:

### Authentication

Create API for Registration User. Create API for Login.

### NoticeController

After login, user can get/create/update/delete their notice. After login, user can view another author's notice that is
in active time (startTime < currentTime< endTime). When user is viewing notice, the TotalView will plus 1

### DocumentController

After login, if user have permission to view notice, then user can download the notice's attachment.

## Technical

- Java 17
- Spring Boot 3.2, Spring Data JPA, Spring Security, Spring Validation, Spring Test
- Mysql, H2 (for Integration test)
- Flyway, Mapstruct, Lombok

## Project Instructions

- Install MySql
- Update Database configuration in resource/application.yml
- Build and run NotificationManagement project.
- Using Postman with included NoticeManagementExam.postman_collection.json in root.

## Project Quality

- Success 6 happy testcases via Postman
- Success 22 Testcases with Integration Test, covered 78% lines of code.

## Key problems-solving strategies

- Max request size is 10MB and attachment is store in database with TINYBLOB type. So if file size or total files size
  in a request is huge, we consider to using cloud storage instead of database.
- Current system is using basic authentication for recognize user to create/view notice. To expand in the future that
  have Login with outsource client (Facebook, Google, Kakao) more secured, I consider to using OAuth2.0
- Current system only have 2 role, viewer user and author notice user, maybe we will add more role like admin, content
  admin for approve/reject the content.
- Currently, I implement soft delete to mark delete notice, we will create a job to hard delete some data that long time
  not using.
- Currently, the notice 's numberOfView is just 1 column in Notice. For expand in future with recommendation data,
  analytic user, it is better to create viewHistory table.
- For high volume traffic, we will implement cache the stateful data that user often request and set more index (default
  is primary key). For more strategies, the key point is handle with multi thread and using microservice for
  development. 


