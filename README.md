# Hotel Management API

Spring Boot 3.2.x demo for managing hotel rooms with OpenAPI (Swagger UI) and in-memory H2 database.

## Tech stack
- Java 21, Spring Boot 3.2.2 (Web, Data JPA, Validation)
- springdoc-openapi-ui 2.3.0
- H2 in-memory database

## Requirements
- JDK 21 (tested: `C:\Program Files\Java\jdk-21`)
- Maven 3.8.9+ (tested: `C:\Users\taquo\.maven\maven-3.8.9\apache-maven-3.8.9\bin\mvn`)

## Run locally
PowerShell example with explicit tool paths:
```powershell
$env:JAVA_HOME="C:\Program Files\Java\jdk-21"
& "C:\Users\taquo\.maven\maven-3.8.9\apache-maven-3.8.9\bin\mvn.cmd" clean spring-boot:run
```

If `JAVA_HOME` and `mvn` are already on PATH:
```powershell
mvn clean spring-boot:run
```

Default ports and consoles:
- API base URL: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs
- H2 console: http://localhost:8080/h2-console (JDBC URL `jdbc:h2:mem:hotel`, user `sa`, blank password)

## Configuration
Key settings in `src/main/resources/application.properties`:
- H2 in-memory URL `jdbc:h2:mem:hotel;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`
- JPA `ddl-auto=update`, `show-sql=true`, formatted SQL
- Swagger paths: `/swagger-ui.html`, `/api-docs`

## Domain model
- `RoomType`: `SINGLE`, `DOUBLE`, `SUITE`, `FAMILY`
- `RoomStatus`: `AVAILABLE`, `OCCUPIED`, `MAINTENANCE`, `CLEANING`
- `Room` fields: `id`, `number` (unique, required), `type`, `price` (>=0, 2 decimal digits), `floor` (>0), `status`

## API endpoints
- `GET /api/v1/rooms` — phân trang + lọc `type`, `status`, `floor`, `minPrice`, `maxPrice`; params `page` (>=0, default 0), `size` (>0, default 10)
- `GET /api/v1/rooms/{id}` — lấy chi tiết phòng
- `POST /api/v1/rooms` — tạo phòng, trả 201 + header `Location`
- `PUT /api/v1/rooms/{id}` — cập nhật phòng
- `DELETE /api/v1/rooms/{id}` — xóa phòng (204)

### Request/response examples
Create room:
```http
POST /api/v1/rooms
Content-Type: application/json

{
	"number": "101",
	"type": "DOUBLE",
	"price": 120.50,
	"floor": 1,
	"status": "AVAILABLE"
}
```

Paginated list with filters:
```http
GET /api/v1/rooms?page=0&size=5&type=SUITE&minPrice=100&maxPrice=300
```

Page response shape (Spring Data `Page`):
```json
{
	"content": [ { "id": 1, "number": "101", "type": "DOUBLE", "price": 120.5, "floor": 1, "status": "AVAILABLE" } ],
	"pageable": { ... },
	"totalElements": 1,
	"totalPages": 1,
	"size": 5,
	"number": 0
}
```

### Validation rules
- `number`: not blank, unique
- `type`: required enum
- `price`: required, DecimalMin 0.0, max 2 fraction digits
- `floor`: required, positive integer
- `status`: required enum

Error format:
```json
{
	"timestamp": "2024-01-01T00:00:00Z",
	"status": 404,
	"error": "Not Found",
	"message": "Room not found",
	"path": "/api/v1/rooms/99"
}
```

## Development notes
- H2 console is enabled for quick inspection; disable in production.
- Repository package layout:
	- `config` – OpenAPI config
	- `controller` – REST APIs with Swagger annotations
	- `dto` – request/response objects
	- `entity` – JPA entities
	- `repository` – Spring Data JPA interfaces
	- `service` – business logic
