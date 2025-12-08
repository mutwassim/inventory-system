package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Bill {
    private String id;
    private String customerName;
    private String customerPhone;
    private List<BillItem> items = new ArrayList<>();
    private double subTotal;
    private double taxAmount;
    private double discountAmount;
    private double netTotal;
    private String date;

    public Bill() {
        this.id = UUID.randomUUID().toString();
        this.date = LocalDateTime.now().toString();
    }

    public void addItem(BillItem item) {
        items.add(item);
        recalculate();
    }

    public void recalculate() {
        subTotal = 0;
        for (BillItem item : items) {
            subTotal += item.getTotalPrice();
        }
        netTotal = subTotal + taxAmount - discountAmount;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
        recalculate();
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
        recalculate();
    }

    public double getNetTotal() {
        return netTotal;
    }

    public List<BillItem> getItems() {
        return items;
    }

    public void setItems(List<BillItem> items) {
        this.items = items;
        recalculate();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
