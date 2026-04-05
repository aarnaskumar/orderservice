# Order Service - Database Configuration Documentation

## Project Overview
**Application Name**: Order Service  
**Database Type**: H2 (Relational Database)  
**Framework**: Spring Boot with Spring Data JPA  
**Java Version**: JDK 21  
**Port**: 8080

---

## Database Connection Details

### H2 In-Memory Database Configuration

| Property | Value | Description |
|----------|-------|-------------|
| **Database URL** | `jdbc:h2:mem:testdb` | In-memory H2 database named "testdb" |
| **Driver Class** | `org.h2.Driver` | H2 database driver |
| **Username** | `sa` | Default H2 admin user |
| **Password** | `password` | Default password for H2 admin |
| **Database Platform** | `org.hibernate.dialect.H2Dialect` | Hibernate dialect for H2 |

### Connection String
```
jdbc:h2:mem:testdb
```

---

## H2 Console Access

### Web-Based H2 Console

- **URL**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- **Status**: Enabled for development
- **Console Path**: `/h2-console`

### H2 Console Login Credentials
| Field | Value |
|-------|-------|
| **JDBC URL** | `jdbc:h2:mem:testdb` |
| **User Name** | `sa` |
| **Password** | `password` |
| **Driver Class** | `org.h2.Driver` |

### Steps to Access H2 Console:
1. Start the Spring Boot application
2. Navigate to `http://localhost:8080/h2-console` in your browser
3. Enter the connection details above
4. Click "Connect" button
5. You can now view and manage the database

---

## Application Configuration

### Current Configuration (application.properties)

```properties
# H2 Database configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# JPA/Hibernate properties
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# H2 Console (for development)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Server port
server.port=8080
```

### Configuration Explanation

#### Database Properties
- **spring.datasource.url**: Connection URL for the database
- **spring.datasource.driverClassName**: JDBC driver for H2
- **spring.datasource.username**: Database user
- **spring.datasource.password**: Database password
- **spring.jpa.database-platform**: Hibernate dialect for H2

#### JPA/Hibernate Properties
- **spring.jpa.hibernate.ddl-auto=create-drop**: 
  - `create-drop`: Creates all tables on application startup and drops them on shutdown
  - Other options: `validate`, `update`, `create`, `none`
- **spring.jpa.show-sql=true**: Logs all SQL queries to console for debugging

#### H2 Console
- **spring.h2.console.enabled=true**: Enables the H2 web console
- **spring.h2.console.path=/h2-console**: Path to access the console

---

## Database Schema

### Order Table Structure

```sql
CREATE TABLE orders (
    order_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price DOUBLE NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Fields Description

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| `order_id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique order identifier |
| `user_id` | BIGINT | NOT NULL | User who created the order |
| `product_id` | BIGINT | NOT NULL | Product being ordered |
| `quantity` | INT | NOT NULL, > 0 | Number of items ordered |
| `price` | DOUBLE | NOT NULL, > 0 | Price per item |
| `status` | VARCHAR(50) | NOT NULL | Order status (CREATED, PAID, CANCELLED) |
| `created_at` | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Timestamp when order was created |

### Order Status Values
- **CREATED**: Order has been created but not yet paid
- **PAID**: Order has been successfully paid
- **CANCELLED**: Order has been cancelled

---

## Alternative Database Configurations

### 1. File-Based H2 Database

For persistent data across application restarts:

```properties
# H2 File-based configuration
spring.datasource.url=jdbc:h2:./data/orderdb;MODE=MySQL
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

**Benefits**: 
- Data persists across application restarts
- Better for local development
- File stored in `./data/orderdb.mv.db`

### 2. MySQL Configuration

For production environment:

```properties
# MySQL configuration
spring.datasource.url=jdbc:mysql://localhost:3306/orderdb
spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
server.port=8080
```

**Dependencies to add** (in pom.xml):
```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>
```

---

## Maven Dependencies for Database

### H2 Database Dependency
```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

### Spring Data JPA Dependency
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

### Complete Database Dependencies in pom.xml
```xml
<!-- Spring Data JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- H2 Database -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Spring Web -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

---

## Testing Database Connection

### Using H2 Console

1. Start application
2. Open browser: `http://localhost:8080/h2-console`
3. Verify connection shows tables under "Orders" in left panel
4. Run test query:
```sql
SELECT * FROM orders;
```

### Using SQL Client

```bash
# Connect to H2 in-memory database
java -cp h2-1.4.200.jar org.h2.tools.Shell \
  -url jdbc:h2:mem:testdb \
  -user sa \
  -password password
```

### Using Spring Boot Console

Check console logs for:
```
Hibernate: CREATE TABLE orders (...)
H2 console available at 'http://localhost:8080/h2-console'
```

---

## API Endpoints for Database Testing

### Create Order
```http
POST http://localhost:8080/orders
Content-Type: application/json

{
  "userId": 1,
  "productId": 101,
  "quantity": 5,
  "price": 29.99
}
```

### Get Order by ID
```http
GET http://localhost:8080/orders/1
```

### Get Paginated Orders
```http
GET http://localhost:8080/orders?page=0&size=10
```

---

## Troubleshooting

### Issue: H2 Console Not Accessible

**Solution**: Verify in application.properties:
```properties
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### Issue: Connection Refused

**Solution**: 
- Ensure application is running
- Check port 8080 is available
- Verify database URL is correct

### Issue: Tables Not Created

**Solution**: Check DDL-auto setting:
```properties
spring.jpa.hibernate.ddl-auto=create-drop  # Creates tables automatically
```

### Issue: Data Not Persisting

**Note**: In-memory H2 database loses data on application restart. Use file-based configuration if persistence needed.

---

## Performance Tips

1. **Enable Connection Pooling**:
```properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
```

2. **Disable SQL Logging in Production**:
```properties
spring.jpa.show-sql=false
```

3. **Use Pagination** for large datasets:
```
GET /orders?page=0&size=10
```

---

## Security Considerations

### Development (Current)
- Using default credentials: `sa` / `password`
- H2 console enabled for debugging
- SQL logging enabled

### Production Recommendations
- Use MySQL/PostgreSQL instead of H2
- Never use default credentials
- Disable H2 console
- Use encrypted passwords
- Enable SSL for database connections
- Implement authentication and authorization

---

## Backup and Recovery

### H2 File-Based Backup
```bash
# Location of H2 database file
./data/orderdb.mv.db
./data/orderdb.trace.db

# Backup command
cp ./data/orderdb.mv.db ./data/orderdb.backup.mv.db
```

### Restore from Backup
```bash
cp ./data/orderdb.backup.mv.db ./data/orderdb.mv.db
```

---

## Additional Resources

- [H2 Database Documentation](http://www.h2database.com/)
- [Spring Data JPA Documentation](https://spring.io/projects/spring-data-jpa)
- [Hibernate Configuration](https://hibernate.org/orm/documentation/)
- [Spring Boot Database Properties](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html)

---

## Contact & Support

For database-related issues or questions:
- Check application logs for error messages
- Verify database connection properties
- Review H2 console for table creation
- Ensure all dependencies are correctly defined in pom.xml

**Last Updated**: April 5, 2026  
**Status**: Active - Development Environment

