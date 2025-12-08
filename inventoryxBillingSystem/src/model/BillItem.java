package model;

public class BillItem {
    private String productId;
    private String productName;
    private double unitPrice;
    private int quantity;
    private double totalPrice;

    public BillItem() {}

    public BillItem(ProductComponent product, int quantity) {
        this.productId = (product instanceof Product) ? ((Product)product).getId() : "BUNDLE";
        this.productName = product.getName();
        this.unitPrice = product.getPrice();
        this.quantity = quantity;
        this.totalPrice = unitPrice * quantity;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
