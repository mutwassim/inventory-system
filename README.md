The project contains approximately 1,980 lines of Java source code inside the src directory.

# Inventory & Billing System (Java No-DB)

A complete, persistent Inventory and Billing system built in Java without a database. It features a modern **Swing Desktop GUI**, uses **JSON files** for local storage, and implements a comprehensive set of **GoF Design Patterns** (Focus on Creational & Structural).

## Features
- **Multi-Business Support**: Create separate stores with isolated data.
- **Role-Based Login**: Admin (Full Access) and User (Restricted).
- **Inventory Management**: Add, Update, Search Products with Bundle support.
- **Smart Billing**: Create bills, auto-update stock, calculate taxes/discounts.
- **Reporting**: Generate Text or PDF (simulated) reports for Sales and Stock.
- **Design Patterns**: Fully architected using Singleton, Factory, Adapter, Facade, etc.

## File Structure
```
src/
  ├── model/          # Entities (Product, User, Bill)
  ├── repository/     # Data Access (File I/O, Proxy)
  ├── service/        # Business Logic (Billing, Inventory, Reports)
  ├── ui/             # Swing GUI Screens & Components
  │   └── screens/    # Individual Panels (Login, Inventory, etc.)
  ├── patterns/       # Pattern Registry
  ├── util/           # Helpers (JSON, Logger, Adapter)
  └── Main.java       # Entry Point
data/                 # JSON Data Storage
  └── [BusinessName]/
      ├── users.json
      ├── inventory.json
      └── bills.json
```

## How to Run

### Option 1: One-Click Script (Windows)
```batch
.\run.bat
```

### Option 2: Manual Compilation
1. **Compile**:
   ```bash
   javac -d bin -sourcepath src src/Main.java
   ```
2. **Run**:
   ```bash
   java -cp bin Main
   ```

## Design Patterns Used
- **Singleton**: Logger, FileStore
- **Factory**: Repositories, Reports
- **Builder**: Bill Generation
- **Facade**: Main System Interface
- **Proxy**: Admin Security, User Repository
- **Decorator**: Tax/Discount Calculation
- ... and more (See `Patterns_Explanation.md`)

## Screenshots
*The application runs as a Desktop Swing Window with tabs for:*
- **Admin Dashboard**: Overview of system status.
- **Inventory**: Table view of products with Add/Edit/Delete actions.
- **Billing**: POS-style interface for creating new bills.
- **Reports**: Generation tools for system insights.
