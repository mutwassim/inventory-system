
import javax.swing.SwingUtilities;
import ui.*;
import ui.screens.*;
import util.Logger;

/**
 * Main Entry Point.
 * Launches the Swing GUI.
 */
public class Main {
    public static void main(String[] args) {
        // [PATTERN: SINGLETON]
        Logger.getInstance().info("System Starting (GUI Mode)...");

        // Launch GUI
        SwingUtilities.invokeLater(() -> {
            WindowManager wm = WindowManager.getInstance();

            // Register Screens
            wm.addScreen("SETUP", new SetupPanel());
            wm.addScreen("LOGIN", new LoginPanel());
            wm.addScreen("ADMIN_DASHBOARD", new AdminDashboard());
            wm.addScreen("INVENTORY", new InventoryPanel());
            wm.addScreen("BILLING", new BillingPanel());
            wm.addScreen("REPORTS", new ReportsPanel());
            wm.addScreen("USERS", new UserManagementPanel()); // Updated

            wm.show();
            wm.showScreen("SETUP");
        });
    }
}
