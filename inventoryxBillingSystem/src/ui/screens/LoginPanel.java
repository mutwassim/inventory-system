package ui.screens;

import service.StoreFacade;
import ui.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginPanel() {
        WindowManager wm = WindowManager.getInstance();
        UIFactory factory = wm.getUIFactory();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel formPanel = factory.createPanel();
        formPanel.setLayout(new GridLayout(4, 2, 10, 10)); // Rows, Cols, HGap, VGap
        formPanel.setBorder(BorderFactory.createTitledBorder("Login"));

        formPanel.add(factory.createLabel("Business Name (Folder):"));
        // For simplicity in this demo, hardcode or use a combo later.
        // We will just load default for now or add a field if needed.
        // Facade is already init with "DefaultStore" in Main, but let's allow re-init?
        // Actually, let's keep it simple: One business per run for now,
        // OR add a "Setup" screen first.
        // Let's assume Facade is initialized in Main before showing this.

        formPanel.add(factory.createLabel("Username:"));
        usernameField = new JTextField(15);
        formPanel.add(usernameField);

        formPanel.add(factory.createLabel("Password:"));
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField);

        loginButton = factory.createButton("Login");

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(formPanel, gbc);

        gbc.gridy = 1;
        add(loginButton, gbc);

        // Event Listener
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
    }

    private void performLogin() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());

        StoreFacade facade = WindowManager.getInstance().getFacade();
        if (facade.login(user, pass)) {
            JOptionPane.showMessageDialog(this, "Welcome " + facade.getCurrentUser().getRole());

            // Navigate based on Role
            if ("ADMIN".equalsIgnoreCase(facade.getCurrentUser().getRole())) {
                WindowManager.getInstance().showScreen("ADMIN_DASHBOARD");
            } else {
                WindowManager.getInstance().showScreen("USER_DASHBOARD"); // or Billing
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
