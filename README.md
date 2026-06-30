# StockPilot

StockPilot is an Inventory & Order Management System built as a Java SE console application.

## 🚀 Tech Stack
- **Language:** Java 17+
- **Build Tool:** Maven
- **Database:** H2 Database (File mode)
- **Testing:** JUnit 5

## ⚙️ Setup and Run

1. **Build the project:**
   ```bash
   mvn clean package
   ```
2. **Run the application:**
   ```bash
   java -jar target/stockpilot-1.0-SNAPSHOT.jar
   ```

## 🗄 Database Schema
- **products:** Stores product catalog (sku, name, category, price, stock).
- **customers:** Stores registered customers (name, email, phone).
- **orders:** Stores customer orders with total amount and date.
- **order_items:** Stores individual items for each order, linking products and orders.

## 📝 Race Condition Write-up
(To be completed in Feature F6)
- **The Issue (Oversold):** Without synchronization, multiple threads might read the same stock quantity and decrement it concurrently, leading to oversold products.
- **The Fix:** Used `synchronized` blocks / DB Row locks to ensure that checking stock and decrementing stock are atomic operations.

## ✅ Feature Checklist
- [ ] Pass Tier: Maven setup, OOP Domain, JDBC persistence, Custom Exceptions, Repository pattern.
- [ ] Good Tier: Transactional orders, Stream reports, CSV import/export.
- [ ] Excellent Tier: Concurrent Flash Sale (No oversold), Clean Architecture, BigDecimal, Auto-export.
