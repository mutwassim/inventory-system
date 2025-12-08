package service;

import model.Bill;
import model.BillItem;
import model.ProductComponent;

/**
 * [PATTERN: BUILDER]
 * Reason: To construct complex Bill objects step-by-step.
 */
public class BillBuilder {
    private Bill bill;

    public BillBuilder() {
        this.bill = new Bill();
    }

    public BillBuilder setCustomer(String name, String phone) {
        bill.setCustomerName(name);
        bill.setCustomerPhone(phone);
        return this;
    }

    public BillBuilder addItem(ProductComponent product, int quantity) {
        bill.addItem(new BillItem(product, quantity));
        return this;
    }

    public Bill build() {
        return bill;
    }
}
