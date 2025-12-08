package service;

import model.Bill;

/**
 * [PATTERN: DECORATOR]
 * Reason: Adding extra features such as discount calculation dynamically without modifying the main Bill class.
 */
public abstract class BillDecorator extends Bill {
    protected Bill decoratedBill;

    public BillDecorator(Bill bill) {
        this.decoratedBill = bill;
        // Copy state so this object acts like the bill
        this.setItems(bill.getItems());
        this.setCustomerName(bill.getCustomerName());
        this.setCustomerPhone(bill.getCustomerPhone());
        this.setId(bill.getId());
        this.setDate(bill.getDate());
        // Initial values
        this.setSubTotal(bill.getSubTotal());
    }

    @Override
    public double getNetTotal() {
        return decoratedBill.getNetTotal();
    }
}
