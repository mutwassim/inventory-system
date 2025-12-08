package service;

import model.Bill;

/**
 * [PATTERN: DECORATOR]
 * Reason: Dynamically applying discounts.
 */
public class DiscountDecorator extends BillDecorator {
    private double discountPercentage;

    public DiscountDecorator(Bill bill, double discountPercentage) {
        super(bill);
        this.discountPercentage = discountPercentage;
    }

    @Override
    public double getNetTotal() {
        double currentTotal = super.getNetTotal();
        double discount = currentTotal * discountPercentage;
        decoratedBill.setDiscountAmount(decoratedBill.getDiscountAmount() + discount);
        return currentTotal - discount;
    }
}
