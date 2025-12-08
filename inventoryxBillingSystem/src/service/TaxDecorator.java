package service;

import model.Bill;

public class TaxDecorator extends BillDecorator {
    private double taxRate;

    public TaxDecorator(Bill bill, double taxRate) {
        super(bill);
        this.taxRate = taxRate;
    }

    @Override
    public double getNetTotal() {
        double currentTotal = super.getNetTotal();
        double tax = currentTotal * taxRate;
        // Update the underlying bill's tax amount for record keeping
        decoratedBill.setTaxAmount(decoratedBill.getTaxAmount() + tax); 
        return currentTotal + tax;
    }
}
