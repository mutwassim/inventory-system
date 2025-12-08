package service;

import model.Product;
import repository.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryService {
    private ProductRepository productRepository;

    public InventoryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void addProduct(Product product) {
        productRepository.save(product);
    }

    public void updateProduct(Product product) {
        productRepository.update(product);
    }

    public void deleteProduct(String id) {
        productRepository.delete(id);
    }

    public Product getProduct(String id) {
        return productRepository.findById(id);
    }

    public List<Product> searchProducts(String query) {
        return productRepository.findAll().stream()
                .filter(p -> p.getName().toLowerCase().contains(query.toLowerCase()) 
                          || p.getCategoryName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    public void deductStock(String productId, int quantity) {
        Product p = getProduct(productId);
        if (p != null) {
            if (p.getStockQuantity() >= quantity) {
                p.setStockQuantity(p.getStockQuantity() - quantity);
                updateProduct(p);
            } else {
                throw new IllegalArgumentException("Insufficient stock for product: " + p.getName());
            }
        }
    }
    
    public List<Product> getLowStockProducts(int threshold) {
        return productRepository.findAll().stream()
                .filter(p -> p.getStockQuantity() <= threshold)
                .collect(Collectors.toList());
    }
}
