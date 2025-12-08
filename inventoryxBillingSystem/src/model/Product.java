package model;

/**
 * [PATTERN: PROTOTYPE]
 * Reason: Implements clone() to allow rapid creation of similar products.
 */
public class Product implements ProductComponent {
    private String id;
    private String name;
    private double price;
    private int stockQuantity;
    private Category category; // Flyweight reference

    public Product() {
    }

    public Product(String id, String name, double price, int stockQuantity, String categoryName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = CategoryFactory.getCategory(categoryName);
    }

    @Override
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getCategoryName() {
        return category != null ? category.getName() : "Uncategorized";
    }

    public String getCategory() {
        return getCategoryName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void showDetails() {
        System.out.println(
                "Product: " + name + " | Price: " + price + " | Stock: " + stockQuantity + " | Category: " + category);
    }

    // [PATTERN: PROTOTYPE]
    @Override
    public Product clone() {
        try {
            return (Product) super.clone();
        } catch (CloneNotSupportedException e) {
            return new Product(this.id, this.name, this.price, this.stockQuantity, this.category.getName());
        }
    }
}
