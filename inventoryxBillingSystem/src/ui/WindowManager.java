package ui;

import javax.swing.*;
import java.awt.*;
import service.StoreFacade;

/**
 * [PATTERN: SINGLETON]
 * Reason: Ensures only one main window exists and manages global navigation
 * state.
 */
public class WindowManager {
    private static WindowManager instance;
    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private StoreFacade facade; // Shared facade instance

    private UIFactory uiFactory;

    private WindowManager() {
        // [PATTERN: ABSTRACT FACTORY] Usage
        this.uiFactory = new ModernThemeFactory();

        frame = new JFrame("Inventory & Billing System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        frame.add(mainPanel);
    }

    public static synchronized WindowManager getInstance() {
        if (instance == null) {
            instance = new WindowManager();
        }
        return instance;
    }

    public void show() {
        frame.setVisible(true);
    }

    public void setFacade(StoreFacade facade) {
        this.facade = facade;
    }

    public StoreFacade getFacade() {
        return facade;
    }

    public void addScreen(String name, JPanel panel) {
        mainPanel.add(panel, name);
    }

    public void showScreen(String name) {
        cardLayout.show(mainPanel, name);
    }

    public UIFactory getUIFactory() {
        return uiFactory;
    }

    public JFrame getFrame() {
        return frame;
    }
}
