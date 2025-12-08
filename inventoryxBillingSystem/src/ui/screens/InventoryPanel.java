package ui.screens;

import model.Product;
import service.StoreFacade;
import ui.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InventoryPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField idField, nameField, priceField, stockField, catField;

    public InventoryPanel() {
        WindowManager wm = WindowManager.getInstance();
        UIFactory factory = wm.getUIFactory();

        setLayout(new BorderLayout());

        // Title
        JPanel top = factory.createPanel();
        top.add(factory.createLabel("INVENTORY MANAGEMENT"));
        add(top, BorderLayout.NORTH);

        // Table
        // [PATTERN: ADAPTER] (Swing's TableModel adapts our List<Product> to JTable)
        String[] columns = { "ID", "Name", "Price", "Stock", "Category" };
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Form
        JPanel form = factory.createPanel();
        form.setLayout(new GridLayout(6, 2));

        form.add(factory.createLabel("ID:"));
        idField = new JTextField();
        form.add(idField);

        form.add(factory.createLabel("Name:"));
        nameField = new JTextField();
        form.add(nameField);

        form.add(factory.createLabel("Price:"));
        priceField = new JTextField();
        form.add(priceField);

        form.add(factory.createLabel("Stock:"));
        stockField = new JTextField();
        form.add(stockField);

        form.add(factory.createLabel("Category:"));
        catField = new JTextField();
        form.add(catField);

        JButton addBtn = factory.createButton("Add Product");
        JButton deleteBtn = factory.createButton("Delete Product");
        JButton refreshBtn = factory.createButton("Refresh");
        JButton backBtn = factory.createButton("Back");

        JPanel btnPanel = factory.createPanel();
        btnPanel.add(addBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(refreshBtn);
        btnPanel.add(backBtn);

        // Layout Form and Buttons
        JPanel bottom = factory.createPanel();
        bottom.setLayout(new BorderLayout());
        bottom.add(form, BorderLayout.CENTER);
        bottom.add(btnPanel, BorderLayout.SOUTH);

        add(bottom, BorderLayout.SOUTH);

        // Listeners
        addBtn.addActionListener(e -> addProduct());
        deleteBtn.addActionListener(e -> deleteSelectedProduct());
        refreshBtn.addActionListener(e -> loadData());
        backBtn.addActionListener(e -> wm.showScreen("ADMIN_DASHBOARD"));
    }

    private void addProduct() {
        try {
            String id = idField.getText();
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());
            String cat = catField.getText();

            WindowManager.getInstance().getFacade().addProduct(id, name, price, stock, cat);
            JOptionPane.showMessageDialog(this, "Product Added");
            loadData();
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid Input: " + ex.getMessage());
        }
    }

    private void deleteSelectedProduct() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product.");
            return;
        }
        String id = (String) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete product " + id + "?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            WindowManager.getInstance().getFacade().deleteProduct(id);
            loadData();
        }
    }

    public void loadData() {
        tableModel.setRowCount(0);
        StoreFacade facade = WindowManager.getInstance().getFacade();
        if (facade == null)
            return;

        List<Product> products = facade.getAllProducts();
        for (Product p : products) {
            tableModel.addRow(
                    new Object[] { p.getId(), p.getName(), p.getPrice(), p.getStockQuantity(), p.getCategory() });
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        priceField.setText("");
        stockField.setText("");
        catField.setText("");
    }
}
