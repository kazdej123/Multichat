package client.view;

import client.controller.Controller;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class ClientView implements View {
    private static final int SCREEN_WIDTH = getScreenSize().width;
    private static final int SCREEN_HEIGHT = getScreenSize().height;

    private static Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    private final JFrame mainFrame = new JFrame("Multichat");

    private final Controller controller = null;

    public ClientView() {
        mainFrame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        mainFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
        mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowClosingListener(mainFrame);

        final JDialog jDialog = new JDialog(mainFrame, "Okno logowania");

        final Container loginDataPanel = new JPanel();

        final GroupLayout groupLayout = new GroupLayout(loginDataPanel);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        final Component usernameLabel = new JLabel("Nazwa użytkownika: ");
        final Component passwordLabel = new JLabel("Hasło: ");
        final Component usernameField = new JTextField("", 20);
        final Component passwordField = new JPasswordField("", 20);

        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup().addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(usernameLabel).addComponent(passwordLabel)).addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(usernameField).addComponent(passwordField)));
        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup().addGroup(createBaselineGroup(groupLayout).addComponent(usernameLabel).addComponent(usernameField)).addGroup(createBaselineGroup(groupLayout).addComponent(passwordLabel).addComponent(passwordField)));

        loginDataPanel.setLayout(groupLayout);

        jDialog.add(loginDataPanel, BorderLayout.NORTH);

        final JComponent loginButtonsPanel = new JPanel();

        final JButton loginButton = createJButton("Zaloguj sie", e -> {
            if (controller != null) {
                controller.login();
            } else {
                jDialog.dispose();
                mainFrame.setVisible(true);
            }
        });

        loginButtonsPanel.add(loginButton);
        loginButtonsPanel.add(createJButton("Anuluj", e -> exitApplication()));
        loginButtonsPanel.add(createJButton("Zarejestruj sie", e -> {
            if (controller != null) {
                controller.register();
            } else {
                JOptionPane.showMessageDialog(jDialog, "Rejestracja przebiegla pomyslnie.", "Rejestracja udana", JOptionPane.INFORMATION_MESSAGE);
            }
        }));
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

    @Override
    public final void init() {
        // TODO
    }

    @Override
    public final void exit() {
        // TODO
    }

    private static GroupLayout.Group createBaselineGroup(@NotNull final GroupLayout groupLayout) {
        return groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE);
    }

    private static JButton createJButton(final String text, final ActionListener actionListener) {
        final JButton jButton = new JButton(text);
        jButton.addActionListener(actionListener);
        return jButton;
    }

    private void addWindowClosingListener(@NotNull final Window window) {
        window.addWindowListener(new WindowAdapter() {
            @Override
            public final void windowClosing(final WindowEvent e) {
                exitApplication();
            }
        });
    }

    private void exitApplication() {
        if (controller != null) {
            controller.exit();
        } else {
            mainFrame.dispose();
        }
        /*if (controller != null) {
            controller.exitApplication();
        } else {
            System.exit(0);
        }*/
    }
}
