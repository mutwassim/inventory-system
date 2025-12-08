package ui.screens;

import ui.*;
import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JPanel {
    public AdminDashboard() {
        WindowManager wm = WindowManager.getInstance();
        UIFactory factory = wm.getUIFactory();

        setLayout(new BorderLayout());

        // Header
        JPanel header = factory.createPanel();
        header.setBackground(Color.LIGHT_GRAY);
        header.add(factory.createLabel("ADMIN DASHBOARD"));
        add(header, BorderLayout.NORTH);

        // Menu Grid
        JPanel menu = factory.createPanel();
        menu.setLayout(new GridLayout(3, 2, 20, 20));
        menu.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnInventory = factory.createButton("Manage Inventory");
        JButton btnUsers = factory.createButton("Manage Users");
        JButton btnBilling = factory.createButton("Billing");
        JButton btnReports = factory.createButton("Reports");
        JButton btnLogout = factory.createButton("Logout");

        menu.add(btnInventory);
        menu.add(btnUsers);
        menu.add(btnBilling);
        menu.add(btnReports);
        menu.add(btnLogout);

        add(menu, BorderLayout.CENTER);

        // Event Listeners
        btnInventory.addActionListener(e -> wm.showScreen("INVENTORY"));
        btnUsers.addActionListener(e -> wm.showScreen("USERS"));
        btnBilling.addActionListener(e -> wm.showScreen("BILLING"));
        btnReports.addActionListener(e -> wm.showScreen("REPORTS"));
        btnLogout.addActionListener(e -> {
            wm.getFacade().logout();
            wm.showScreen("LOGIN");
        });
    }
}
