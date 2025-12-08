package ui.screens;

import service.StoreFacade;
import ui.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SetupPanel extends JPanel {
    private JTextField businessNameField;
    private JButton loadButton;

    public SetupPanel() {
        WindowManager wm = WindowManager.getInstance();
        UIFactory factory = wm.getUIFactory();

        setLayout(new GridBagLayout());

        JPanel panel = factory.createPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));

        panel.add(factory.createLabel("Enter Business Name to Load/Create:"));
        businessNameField = new JTextField("DefaultStore");
        panel.add(businessNameField);

        loadButton = factory.createButton("Load System");
        panel.add(loadButton);

        add(panel);

        loadButton.addActionListener((ActionEvent e) -> {
            String name = businessNameField.getText().trim();
            if (name.isEmpty())
                name = "DefaultStore";

            // Initialize Facade
            StoreFacade facade = new StoreFacade(name);
            wm.setFacade(facade);

            wm.showScreen("LOGIN");
        });
    }
}
