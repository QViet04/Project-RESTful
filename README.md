# Hotel Management API

Spring Boot demo for managing hotel rooms with Swagger documentation.

## Requirements
- Java 21 (tested with `C:\Program Files\Java\jdk-21`)
- Maven 3.8.9+ (tested with `C:\Users\taquo\.maven\maven-3.8.9\apache-maven-3.8.9\bin\mvn`)

## How to run
PowerShell (Windows), using the tested JDK and Maven:
```powershell
$env:JAVA_HOME="C:\Program Files\Java\jdk-21"
& "C:\Users\taquo\.maven\maven-3.8.9\apache-maven-3.8.9\bin\mvn.cmd" clean spring-boot:run
```

If `JAVA_HOME`/`mvn` are already on PATH, a simpler form works:
```powershell
mvn clean spring-boot:run
```

Swagger UI: http://localhost:8080/swagger-ui.html
API docs (JSON): http://localhost:8080/api-docs

## Package layout
- `com.example.hotelmanagement.config` – OpenAPI configuration
- `controller` – REST APIs with Swagger annotations
- `dto` – request/response objects
- `entity` – JPA entities
- `repository` – Spring Data JPA interfaces
- `service` – business logic
