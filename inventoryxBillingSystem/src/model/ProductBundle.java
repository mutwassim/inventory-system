package model;

import java.util.ArrayList;
import java.util.List;

/**
 * [PATTERN: COMPOSITE]
 * Reason: Represents a bundle of products (e.g., "Gamer Pack") as a single component.
 */
public class ProductBundle implements ProductComponent {
    private String name;
    private List<ProductComponent> items = new ArrayList<>();

    public ProductBundle(String name) {
        this.name = name;
    }

    public void addProduct(ProductComponent product) {
        items.add(product);
    }

    public void removeProduct(ProductComponent product) {
        items.remove(product);
    }

    @Override
    public double getPrice() {
        double total = 0;
        for (ProductComponent p : items) {
            total += p.getPrice();
        }
        return total;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void showDetails() {
        System.out.println("Bundle: " + name + " (Contains " + items.size() + " items)");
        for (ProductComponent p : items) {
            p.showDetails();
        }
    }

    // [PATTERN: PROTOTYPE]
    @Override
    public ProductBundle clone() {
        ProductBundle bundle = new ProductBundle(this.name);
        for (ProductComponent p : items) {
            bundle.addProduct(p.clone());
        }
        return bundle;
    }
}
