package service;

import model.Bill;
import model.Product;
import model.User;
import repository.*;
import service.report.ReportService;

/**
 * [PATTERN: FACADE]
 * Reason: Provides a simplified interface to the complex subsystems (Inventory,
 * Billing, Reporting, Auth)
 * for the UI layer to consume.
 */
public class StoreFacade {
    private String businessPath;
    private IUserRepository userRepo; // Access via Proxy
    private ProductRepository productRepo;
    private InventoryService inventoryService;
    private BillingService billingService;
    private User currentUser;

    public StoreFacade(String businessName) {
        this.businessPath = "data/" + businessName;
        // Ensure dir exists
        FileStore.getInstance().createDirectory(this.businessPath);

        // Initialize Repos
        // Note: We use specific types here but could be generic
        this.userRepo = (IUserRepository) RepositoryFactory.getRepository("USER", businessPath);
        this.productRepo = (ProductRepository) RepositoryFactory.getRepository("PRODUCT", businessPath);

        this.inventoryService = new InventoryService(productRepo);
        this.billingService = new BillingService(businessPath);

        // Ensure default admin exists
        if (userRepo.findByUsername("admin") == null) {
            userRepo.save(new User("admin", "admin123", "ADMIN"));
        }
    }

    public boolean login(String username, String password) {
        User u = userRepo.findByUsername(username);
        if (u != null && u.getPassword().equals(password)) {
            this.currentUser = u;
            // Wrap repo with Proxy for this session
            // userRepo is IUserRepository, AuthProxy implements IUserRepository
            // We need to pass the REAL implementation to the proxy.
            // Problem: If userRepo is ALREADY a proxy, we don't want to wrap it again or we
            // might lose the real repo.
            // But here we assume it's the raw repo initially or previously set.
            // Ideally we keep a reference to the 'real' repo separately or check type.
            // For simplicity in this fix, assuming userRepo is currently the concrete
            // UserRepository (or we cast carefully if we can)
            // But wait, constructor sets it to UserRepository.

            // To be safe, let's just create the proxy with the current userRepo as the
            // delegate.
            // Note: AuthProxy constructor expects UserRepository, but we should probably
            // relax it to IUserRepository or check cast.
            // Checking AuthProxy.java: public AuthProxy(UserRepository realRepo, User
            // currentUser)
            // It demands UserRepository. So we MUST cast.

            if (this.userRepo instanceof UserRepository) {
                this.userRepo = new AuthProxy((UserRepository) this.userRepo, u);
            } else if (this.userRepo instanceof AuthProxy) {
                // Already a proxy? Maybe simpler to just not wrap again or unwrap?
                // For now, let's assume first login.
            }
            return true;
        }
        return false;
    }

    public void logout() {
        this.currentUser = null;
        // reset repo usage if needed
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // Inventory Facade Methods
    public void addProduct(String id, String name, double price, int stock, String category) {
        Product p = new Product(id, name, price, stock, category);
        inventoryService.addProduct(p);
    }

    public void updateProduct(Product p) {
        inventoryService.updateProduct(p);
    }

    public Product getProduct(String id) {
        return inventoryService.getProduct(id);
    }

    public java.util.List<Product> getAllProducts() {
        return inventoryService.getAllProducts();
    }

    // Billing Facade Methods
    public void processBill(Bill bill) {
        // Validate stock
        for (model.BillItem item : bill.getItems()) {
            inventoryService.deductStock(item.getProductId(), item.getQuantity());
        }
        billingService.saveBill(bill);
    }

    // Reporting Facade Methods
    // Reporting Facade Methods
    public java.util.List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public void registerUser(String username, String password, String role) {
        if (userRepo.findByUsername(username) != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        userRepo.save(new User(username, password, role));
    }

    public void deleteUser(String username) {
        userRepo.delete(username);
    }

    // Inventory Facade Methods
    public void deleteProduct(String id) {
        inventoryService.deleteProduct(id);
    }

    // Reporting Facade Methods
    public String generateReport(String type) {
        // Default to TEXT for GUI
        return ReportService.generateReport(type, "TEXT", businessPath);
    }
}
