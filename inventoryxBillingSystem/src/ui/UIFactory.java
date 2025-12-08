package ui;

import javax.swing.*;

/**
 * [PATTERN: ABSTRACT FACTORY]
 * Reason: To create families of UI components (like Buttons, Panels) with a
 * consistent theme.
 */
public interface UIFactory {
    JButton createButton(String text);

    JPanel createPanel();

    JLabel createLabel(String text);
}
