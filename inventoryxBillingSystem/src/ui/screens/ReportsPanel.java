package ui.screens;

import ui.*;
import javax.swing.*;
import java.awt.*;

public class ReportsPanel extends JPanel {
    private JTextArea outputArea;

    public ReportsPanel() {
        WindowManager wm = WindowManager.getInstance();
        UIFactory factory = wm.getUIFactory();

        setLayout(new BorderLayout());

        JPanel top = factory.createPanel();
        JButton summaryBtn = factory.createButton("Sales Summary");
        JButton stockBtn = factory.createButton("Low Stock");
        JButton backBtn = factory.createButton("Back");

        top.add(summaryBtn);
        top.add(stockBtn);
        top.add(backBtn);

        add(top, BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Logic
        summaryBtn.addActionListener(e -> generateReport("SUMMARY"));
        stockBtn.addActionListener(e -> generateReport("STOCK"));
        backBtn.addActionListener(e -> wm.showScreen("ADMIN_DASHBOARD"));
    }

    private void generateReport(String type) {
        outputArea.setText("Generating " + type + " report...\n");
        String report = WindowManager.getInstance().getFacade().generateReport(type);
        outputArea.setText(report);
    }
}
