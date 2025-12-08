# Design Patterns: The Complete Guide

This document explains the Design Patterns used in the system, combining formal definitions, simple "ELI5" analogies, detailed technical implementation details, and an analysis of what would happen if we didn't use them.

---

## 🏗️ CREATIONAL PATTERNS
*Tools for creating objects (LEGO blocks) without making a mess.*

### 1. Singleton (The "One Ruler")
* **Definition:** Ensures a class has only one instance and provides a global point of access to it.
* **ELI5:** Imagine a kingdom with **only one King**. If you call for "The King", you always get the same person. You never accidentally create a second King.
* **Implementation:** 
    * **Where:** `src/util/Logger.java`, `src/repository/FileStore.java`.
    * **How:** We made the constructor `private` so no one can use `new Logger()`. We provided a `static getInstance()` method that returns the single shared instance.
    * **Why:** We need a single point of truth for file access.
* **⚠️ What if we didn't use it?**
    * If multiple parts of the app tried to write to `inventory.json` at the exact same time using different file writer objects, the file would get corrupted or data would be overwritten/lost (Race Conditions).

### 2. Factory Method (The "Toy Machine")
* **Definition:** Defines an interface for creating an object, but lets subclasses decide which class to instantiate.
* **ELI5:** Instead of building a toy car by hand every time, you ask a **Machine**. You say "Give me a Car", and it handles the complex building part.
* **Implementation:**
    * **Where:** `src/repository/RepositoryFactory.java`.
    * **How:** `RepositoryFactory.getRepository(type)` takes a string like "USER" or "PRODUCT" and returns the correct `IRepository` implementation.
    * **Why:** Decouples the Service layer from the Repository layer.
* **⚠️ What if we didn't use it?**
    * Every time we wanted a repository, we'd have to write `new UserRepository(path, config...)` inside our Service classes. If we changed how repositories are created (e.g., adding a database connection), we'd have to find and change code in 50 different places.

### 3. Abstract Factory (The "Theme Kit")
* **Definition:** Provides an interface for creating families of related or dependent objects without specifying their concrete classes.
* **ELI5:** You have a LEGO "Space Kit" and a "Castle Kit". If you pick Space, you get aliens. If you pick Castle, you get knights. You don't mix them up.
* **Implementation:**
    * **Where:** `src/service/report/ReportPatterns.java`.
    * **How:** The `ReportFactory` interface forces creating a *family* of products (Summary Report, Stock Report) that match a theme (PDF or Text).
    * **Why:** Ensures consistency across report formats.
* **⚠️ What if we didn't use it?**
    * We might accidentally mix formats, creating a Text Header with PDF Content, or checking `if (type == "PDF")` in every single report generation method, leading to messy, unmaintainable `if-else` chains.

### 4. Builder (The "Custom Pizza")
* **Definition:** Separates the construction of a complex object from its representation so that the same construction process can create different representations.
* **ELI5:** Ordering a pizza. You say: "Start with crust, add cheese, add pepperoni... okay, *now* bake it."
* **Implementation:**
    * **Where:** `src/service/BillBuilder.java`.
    * **How:** Method chaining: `new BillBuilder().setCustomer(...).addItem(...).build()`. It buffers the steps and only returns a valid `Bill` object at the end.
    * **Why:** `Bill` objects are complex and require many steps to be valid.
* **⚠️ What if we didn't use it?**
    * We would need a giant constructor: `new Bill(id, customer, itemsList, date, total, tax...)`. This is hard to read and easy to break (passing `null` by mistake).

### 5. Prototype (The "Photocopier")
* **Definition:** Specifies the kinds of objects to create using a prototypical instance, and creates new objects by copying this prototype.
* **ELI5:** You drew a perfect picture. Instead of drawing it again, you use a photocopier to make a copy.
* **Implementation:**
    * **Where:** `src/model/Product.java`.
    * **How:** Implements `Cloneable` interface. The `clone()` method creates a new object with the exact same values as the original.
    * **Why:** Efficiently creating variants of products (e.g., "Gaming Laptop v2" based on "v1").
* **⚠️ What if we didn't use it?**
    * To duplicate a product, we'd have to manually copy every field: `new Product(old.id, old.name, old.price...)`. If we added a new field to Product later, we'd forget to update the copy logic, leading to bugs.

---

## 🔌 STRUCTURAL PATTERNS
*Tools for connecting different blocks together.*

### 6. Adapter (The "Travel Plug")
* **Definition:** Converts the interface of a class into another interface the clients expect.
* **ELI5:** Your phone charger doesn't fit the wall socket in another country. You use an **Adapter** to make it fit.
* **Implementation:**
    * **Where:** `src/util/LoggerAdapter.java`.
    * **How:** It implements our modern `ILogger` interface but internally calls methods on the legacy `FileLoggerSystem` class.
    * **Why:** integrating old code without rewriting it.
* **⚠️ What if we didn't use it?**
    * We would have to rewrite the entire `FileLoggerSystem` to match our interface, or pollute our new code with calls to the old, ugly API.

### 7. Bridge (The "Remote Control")
* **Definition:** Decouples an abstraction from its implementation so that the two can vary independently.
* **ELI5:** Your TV Remote works with many different TVs. You can change the TV without changing the remote.
* **Implementation:**
    * **Where:** `src/repository/IRepository.java` (Abstraction) vs `src/repository/FileRepository.java` (Implementation).
    * **How:** The interface defines *what* we do (`save`, `find`), and the implementation defines *how* (write to JSON).
    * **Why:** Allows swapping storage (JSON -> SQL) without breaking the app.
* **⚠️ What if we didn't use it?**
    * The UI would be tightly coupled to File Storage: `fileSystem.saveOrder()`. If we wanted to switch to a Database, we'd have to rewrite the entire UI layer.

### 8. Composite (The "Box of Toys")
* **Definition:** Composes objects into tree structures to represent part-whole hierarchies.
* **ELI5:** You can wrap a single toy or a whole box of toys. To the wrapping paper, they are both just "Items".
* **Implementation:**
    * **Where:** `src/model/ProductBundle.java`.
    * **How:** `ProductBundle` implements `ProductComponent` and contains a list of other `ProductComponent`s. It calculates its price by summing its children.
    * **Why:** Treating single products and bundles of products uniformly.
* **⚠️ What if we didn't use it?**
    * The Bill calculation logic would be complex: `if (item is Product) price = x; else if (item is Bundle) loop through children...`. This duplication breeds bugs.

### 9. Decorator (The "Toppings")
* **Definition:** Attaches additional responsibilities to an object dynamically.
* **ELI5:** A plain donut. Dip it in chocolate (decorate). Sprinkle nuts (decorate again). It's still a donut, but fancier.
* **Implementation:**
    * **Where:** `src/service/BillDecorator.java`.
    * **How:** Classes like `TaxDecorator` wrap a `Bill` object. When you ask for `getNetTotal()`, it calls the inner bill's total and adds its own calculation.
    * **Why:** Dynamic pricing rules without inheritance limits.
* **⚠️ What if we didn't use it?**
    * We would need a rigid inheritance tree: `TaxedBill`, `DiscountedBill`, `TaxedAndDiscountedBill`. It leads to a "class explosion" where every combination needs a new class.

### 10. Facade (The "Smart Home Button")
* **Definition:** Provides a unified interface to a set of interfaces in a subsystem.
* **ELI5:** Instead of doing 10 things manually (dim lights, turn on TV...), you press one button: **"Movie Mode"**.
* **Implementation:**
    * **Where:** `src/service/StoreFacade.java`.
    * **How:** Defines simple methods like `processBill()` that coordinate the `InventoryService`, `BillingService`, and `FileStore` internally.
    * **Why:** Makes the UI code simple and clean.
* **⚠️ What if we didn't use it?**
    * The UI button would have 50 lines of code: "Check stock, subtract stock, create bill, save bill, clear UI". If the logic changed, we'd have to update every button in the UI.

### 11. Flyweight (The "Stamp")
* **Definition:** Uses sharing to support large numbers of fine-grained objects efficiently.
* **ELI5:** Instead of writing "APPROVED" by hand 100 times, you use **one rubber stamp** 100 times.
* **Implementation:**
    * **Where:** `src/model/Category.java` managed by `CategoryFactory`.
    * **How:** We cache `Category` objects. If 50 products are "Electronics", they all reference the *same* `Category` object in memory.
    * **Why:** Saves RAM when dealing with thousands of products.
* **⚠️ What if we didn't use it?**
    * We would create thousands of identical String objects "Electronics". In a large system, this wastes memory and slows down garbage collection.

### 12. Proxy (The "Bodyguard")
* **Definition:** Provides a surrogate or placeholder for another object to control access to it.
* **ELI5:** You want to talk to the Celebrity, but first you meet the Bodyguard. He checks if you are on the list.
* **Implementation:**
    * **Where:** `src/repository/AuthProxy.java`.
    * **How:** The Proxy implements `IUserRepository`. It intercepts `delete()` calls, checks if the current user is ADMIN, and only *then* passes the call to the real Repository.
    * **Why:** Security and Access Control.
* **⚠️ What if we didn't use it?**
    * We would have to paste `if (user.isAdmin())` checks inside every single delete button in the UI. Someone would eventually forget, creating a security hole.

---

## 🧠 BEHAVIORAL PATTERNS
*Tools for how objects talk to each other.*

### 13. Iterator (The "Music Playlist")
* **Definition:** Provides a way to access the elements of an aggregate object sequentially without exposing its underlying representation.
* **ELI5:** You press "Next Song". You don't care how the phone stores the songs, you just want the **next** one.
* **Implementation:**
    * **Where:** `src/service/InventoryService.java`.
    * **How:** Logic uses `for (Product p : products)`. Java's `Iterator` handles the traversal behind the scenes.
    * **Why:** Looping without exposing internal structure.
* **⚠️ What if we didn't use it?**
    * We would have to write `for (int i=0; i<products.size(); i++)` everywhere. If we changed from `ArrayList` to `LinkedList` or `HashSet`, all those loops would break or become inefficient.

### 14. Observer (The "Newsletter")
* **Definition:** Defines a one-to-many dependency between objects so that when one object changes state, all its dependents are notified and updated automatically.
* **ELI5:** You subscribe to a channel. When they post, you get notified automatically.
* **Implementation:**
    * **Where:** `src/ui/screens/*.java`.
    * **How:** `button.addActionListener(...)`. The Button is the Subject; your code is the Observer. When clicked, it notifies you.
    * **Why:** Event-driven UI programming.
* **⚠️ What if we didn't use it?**
    * The program would have to constantly ask the button "Are you clicked? Are you clicked?" (Polling), which freezes the computer and wastes CPU.

### 15. Strategy (The "Video Game Weapon")
* **Definition:** Defines a family of algorithms, encapsulates each one, and makes them interchangeable.
* **ELI5:** Fighting a boss. You swap from "Sword" to "Bow". Your strategy changes, but you are still "Attacking".
* **Implementation:**
    * **Where:** `src/service/InventoryService.java` (Filtering).
    * **How:** Passing a lambda `p -> p.getPrice() > 100` is a Strategy. The `filter` method uses this strategy to decide what to keep.
    * **Why:** Flexible algorithms at runtime.
* **⚠️ What if we didn't use it?**
    * We would need separate methods for every filter: `findProductOver100()`, `findProductUnder50()`, `findProductByName()...`. Infinite copy-paste code.

### 16. Template Method (The "Recipe Card")
* **Definition:** Defines the skeleton of an algorithm in an operation, deferring some steps to subclasses.
* **ELI5:** A cake recipe: "Mix -> Bake -> Frost". Ingredients change, but steps stay the same.
* **Implementation:**
    * **Where:** `src/service/report/Report.java`.
    * **How:** The `generate()` method defines the workflow. Subclasses just provide the data string, but the structure of returning a result is fixed.
    * **Why:** Enforcing a workflow while allowing custom steps.
* **⚠️ What if we didn't use it?**
    * Every report would be written from scratch. Some might forget to save the file, others might forget to format the header. The system would be inconsistent.
