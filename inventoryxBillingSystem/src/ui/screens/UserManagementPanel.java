package ui.screens;

import model.User;
import service.StoreFacade;
import ui.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserManagementPanel extends JPanel {
    private JTable userTable;
    private DefaultTableModel tableModel;

    public UserManagementPanel() {
        WindowManager wm = WindowManager.getInstance();
        UIFactory factory = wm.getUIFactory();

        setLayout(new BorderLayout());

        // Top Controls
        JPanel topPanel = factory.createPanel();
        JButton addBtn = factory.createButton("Add User");
        JButton deleteBtn = factory.createButton("Delete User");
        JButton refreshBtn = factory.createButton("Refresh");
        JButton backBtn = factory.createButton("Back");

        topPanel.add(addBtn);
        topPanel.add(deleteBtn);
        topPanel.add(refreshBtn);
        topPanel.add(backBtn);

        add(topPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(new String[] { "Username", "Role" }, 0);
        userTable = new JTable(tableModel);
        add(new JScrollPane(userTable), BorderLayout.CENTER);

        // Listeners
        addBtn.addActionListener(e -> showAddUserDialog());
        deleteBtn.addActionListener(e -> deleteSelectedUser());
        refreshBtn.addActionListener(e -> refreshTable());
        backBtn.addActionListener(e -> wm.showScreen("ADMIN_DASHBOARD"));

        // Initial Load
        // Ideally this happens on 'show', but constructor works if refreshed manually
        // or re-created
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        StoreFacade facade = WindowManager.getInstance().getFacade();
        try {
            List<User> users = facade.getAllUsers(); // Assuming getAllUsers returns list
            // Note: getAllUsers is protected by Proxy (Admin only).
            // If logged in user is not admin, this might return empty or throw checks?
            // AuthProxy handles findAll checks.

            for (User u : users) {
                tableModel.addRow(new Object[] { u.getUsername(), u.getRole() });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + e.getMessage());
        }
    }

    private void showAddUserDialog() {
        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        String[] roles = { "USER", "ADMIN" };
        JComboBox<String> roleCombo = new JComboBox<>(roles);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Role:"));
        panel.add(roleCombo);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New User", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String u = usernameField.getText();
            String p = new String(passwordField.getPassword());
            String r = (String) roleCombo.getSelectedItem();

            try {
                WindowManager.getInstance().getFacade().registerUser(u, p, r);
                refreshTable();
                JOptionPane.showMessageDialog(this, "User added successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to add user: " + ex.getMessage());
            }
        }
    }

    private void deleteSelectedUser() {
        int row = userTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user.");
            return;
        }

        String username = (String) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete user " + username + "?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                WindowManager.getInstance().getFacade().deleteUser(username);
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to delete user: " + ex.getMessage());
            }
        }
    }
}
