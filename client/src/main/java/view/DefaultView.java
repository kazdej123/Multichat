package view;

import controller.Controller;
import org.jetbrains.annotations.NotNull;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class DefaultView implements View {
    private static final int SCREEN_WIDTH = getScreenSize().width;
    private static final int SCREEN_HEIGHT = getScreenSize().height;

    private static Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    public DefaultView() {
        final JFrame jFrame = new JFrame("Multichat");

        final JDialog jDialog = new JDialog(jFrame, "Okno logowania", true);

        final JPanel loginDataPanel = new JPanel();

        final GroupLayout groupLayout = new GroupLayout(loginDataPanel);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        final JLabel usernameLabel = new JLabel("Nazwa użytkownika: ");
        final JLabel passwordLabel = new JLabel("Hasło: ");
        final JTextField usernameField = new JTextField("", 20);
        final JPasswordField passwordField = new JPasswordField("", 20);

        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup().addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(usernameLabel).addComponent(passwordLabel)).addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(usernameField).addComponent(passwordField)));
        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup().addGroup(createBaselineGroup(groupLayout).addComponent(usernameLabel).addComponent(usernameField)).addGroup(createBaselineGroup(groupLayout).addComponent(passwordLabel).addComponent(passwordField)));

        loginDataPanel.setLayout(groupLayout);

        jDialog.add(loginDataPanel, BorderLayout.NORTH);

        final JPanel loginButtonsPanel = new JPanel();

        final JButton loginButton = createJButton("Zaloguj sie", null); // TODO

        loginButtonsPanel.add(loginButton);
        loginButtonsPanel.add(createJButton("Anuluj", null)); // TODO
        loginButtonsPanel.add(createJButton("Zarejestruj sie", null)); // TODO
        loginButtonsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        jDialog.add(loginButtonsPanel, BorderLayout.SOUTH);
        jDialog.getRootPane().setDefaultButton(loginButton);
        jDialog.pack();
        jDialog.setLocation((SCREEN_WIDTH - jDialog.getWidth()) / 2, (SCREEN_HEIGHT - jDialog.getHeight()) / 2);
        jDialog.setResizable(false);
        jDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowClosingListener(jDialog);
        jDialog.setVisible(true);
    }

    private static GroupLayout.ParallelGroup createBaselineGroup(@NotNull final GroupLayout groupLayout) {
        return groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE);
    }

    private static JButton createJButton(final String text, final ActionListener actionListener) {
        final JButton jButton = new JButton(text);
        jButton.addActionListener(actionListener);
        return jButton;
    }

    private static void addWindowClosingListener(@NotNull final Window window) {
        window.addWindowListener(new WindowAdapter() {
            @Override
            public final void windowClosing(final WindowEvent e) {
                exit();
            }
        });
    }

    private static void exit() {
        final Controller controller = null;
        if (controller != null) {
            controller.exit();
        }
    }
}
