# PROJECT DOCUMENTATION
## Inventory & Billing System

### 1. Abstract
This project implements a comprehensive Inventory and Billing System using Java. The core objective was to demonstrate the practical application of Object-Oriented Programming (OOP) principles and Gang of Four (GoF) Design Patterns. The system features a distinct **Swing-based Graphical User Interface (GUI)** and runs offline using a file-based JSON storage mechanism, eliminating the need for a traditional database while maintaining data persistence.

### 2. Introduction
Traditional billing systems often rely on heavy database engines. This project explores a lightweight, file-based architecture suitable for small-medium businesses or embedded systems. It supports multiple independent business profiles, role-based access control, and dynamic reporting.

### 3. Architecture
The system follows a **Layered Architecture** with a GUI frontend:
1.  **UI Layer**: Java Swing components (`ui.screens` package) providing a responsive Desktop interface.
2.  **Facade Layer**: `StoreFacade` provides a unified API, decoupling the UI from business logic.
3.  **Service Layer**: Encapsulates business rules (`BillingService`, `InventoryService`, `ReportService`).
4.  **Repository Layer**: Abstracts data access (`FileRepository`, `AuthProxy`), enabling persistence transparency.
5.  **Data Layer**: JSON files stored per-business in the local file system.

### 4. Design Patterns Implementation
(See `Patterns_Explanation.md` for detailed breakdown)
The project heavily utilizes **Creational** and **Structural** patterns to ensure scalability and maintainability:
-   **Creational**: Singleton (Config), Factory (Repos), Builder (Bills), Prototype (Cloning), Abstract Factory (Reports).
-   **Structural**: Adapter (Logging), Bridge (Storage), Composite (Bundles), Decorator (Pricing), Facade (API), Flyweight (Categories), Proxy (Security).
*Note: Behavioral patterns such as Iterator and Observer are inherently used via Java Collections and Swing Event Model.*

### 5. Modules
-   **Authentication**: Secure login/logout with role-based routing (Admin vs User).
-   **Inventory Management**: Full CRUD operations for Products, Stock levels, and Bundles.
-   **Billing System**: POS interface for adding items, calculating totals with Taxes/Discounts, and stock deduction.
-   **Reporting**: Generates Sales Summaries and Low Stock warnings in Text or Simulated PDF formats.
-   **User Management**: Admin-only panel for registering and removing system users.

### 6. Conclusion
The project successfully delivers a robust, pattern-rich application that meets all functional requirements. It demonstrates how complex design patterns can solve common software design problems like object creation, structural decoupledness, and interface adaption within a modern desktop application context.
