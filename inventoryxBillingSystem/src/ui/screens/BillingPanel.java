package ui.screens;

import model.Bill;
import model.Product;
import service.BillBuilder;
import service.DiscountDecorator;
import service.TaxDecorator;
import service.StoreFacade;
import ui.*;
import ui.WindowManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BillingPanel extends JPanel {
    private DefaultTableModel cartModel;
    private JTextField phoneField, nameField, prodIdField, qtyField;
    private JLabel totalLabel;
    private BillBuilder builder;

    public BillingPanel() {
        WindowManager wm = WindowManager.getInstance();
        UIFactory factory = wm.getUIFactory();

        setLayout(new BorderLayout());

        // Customer Info
        JPanel top = factory.createPanel();
        top.setBorder(BorderFactory.createTitledBorder("Customer Details"));
        top.add(factory.createLabel("Phone:"));
        phoneField = new JTextField(10);
        top.add(phoneField);
        top.add(factory.createLabel("Name:"));
        nameField = new JTextField(10);
        top.add(nameField);

        JButton startBtn = factory.createButton("Start Bill");
        top.add(startBtn);

        add(top, BorderLayout.NORTH);

        // Cart Table
        String[] cols = { "Product ID", "Name", "Price", "Qty", "Total" };
        cartModel = new DefaultTableModel(cols, 0);
        JTable table = new JTable(cartModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Item Entry & Actions
        JPanel bottom = factory.createPanel();
        bottom.setLayout(new GridLayout(3, 1));

        JPanel entry = factory.createPanel();
        entry.add(factory.createLabel("Prod ID:"));
        prodIdField = new JTextField(8);
        entry.add(prodIdField);
        entry.add(factory.createLabel("Qty:"));
        qtyField = new JTextField(4);
        entry.add(qtyField);
        JButton addBtn = factory.createButton("Add Item");
        entry.add(addBtn);

        bottom.add(entry);

        JPanel totals = factory.createPanel();
        totalLabel = factory.createLabel("Total: 0.0");
        totals.add(totalLabel);
        JButton checkoutBtn = factory.createButton("Checkout");
        totals.add(checkoutBtn);
        JButton backBtn = factory.createButton("Back");
        totals.add(backBtn);
        bottom.add(totals);

        add(bottom, BorderLayout.SOUTH);

        // Logic
        startBtn.addActionListener(e -> {
            builder = new BillBuilder();
            builder.setCustomer(nameField.getText(), phoneField.getText());
            cartModel.setRowCount(0);
            totalLabel.setText("Total: 0.0");
            JOptionPane.showMessageDialog(this, "New Bill Started");
        });

        addBtn.addActionListener(e -> addItem());

        checkoutBtn.addActionListener(e -> checkout());

        backBtn.addActionListener(e -> wm.showScreen("ADMIN_DASHBOARD")); // Or Main Menu
    }

    private void addItem() {
        if (builder == null) {
            JOptionPane.showMessageDialog(this, "Click Start Bill first!");
            return;
        }

        String pid = prodIdField.getText();
        String sQty = qtyField.getText();

        try {
            int qty = Integer.parseInt(sQty);
            StoreFacade facade = WindowManager.getInstance().getFacade();
            Product p = facade.getProduct(pid);

            if (p != null) {
                if (qty <= p.getStockQuantity()) {
                    builder.addItem(p, qty);
                    double lineTotal = p.getPrice() * qty;
                    cartModel.addRow(new Object[] { p.getId(), p.getName(), p.getPrice(), qty, lineTotal });
                    updateRunningTotal();
                } else {
                    JOptionPane.showMessageDialog(this, "Insufficient Stock! Available: " + p.getStockQuantity());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Product Not Found");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Quantity");
        }
    }

    private void updateRunningTotal() {
        // Just a rough display, real calcs happen on checkout
        // Or we could build a temp bill to check
        // For now, let's sum up the table
        double sum = 0;
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            sum += (Double) cartModel.getValueAt(i, 4);
        }
        totalLabel.setText("SubTotal: " + sum);
    }

    private void checkout() {
        if (builder == null)
            return;
        Bill bill = builder.build();

        // Ask for Tax/Discount
        String sTax = JOptionPane.showInputDialog(this, "Enter Tax % (e.g. 0.10):", "0.0");
        String sDisc = JOptionPane.showInputDialog(this, "Enter Discount % (e.g. 0.05):", "0.0");

        try {
            double tax = Double.parseDouble(sTax);
            double disc = Double.parseDouble(sDisc);

            // [PATTERN: DECORATOR] usage UI side
            Bill finalBill = new TaxDecorator(new DiscountDecorator(bill, disc), tax);

            // Apply back to original logic if needed or just save
            bill.setTaxAmount(finalBill.getTaxAmount());
            bill.setDiscountAmount(finalBill.getDiscountAmount());

            WindowManager.getInstance().getFacade().processBill(bill);
            JOptionPane.showMessageDialog(this, "Bill Saved! Net Total: " + finalBill.getNetTotal());

            // Reset
            builder = null;
            cartModel.setRowCount(0);
            phoneField.setText("");
            nameField.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error processing bill: " + ex.getMessage());
        }
    }
}
