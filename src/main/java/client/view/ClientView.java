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

    private final JFrame mainFrame = new JFrame("Multichat");
    private final JDialog loginDialog = new JDialog(mainFrame, "Okno logowania");

    private Controller controller = null;

    private static Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    public ClientView() {
        mainFrame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        mainFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
        mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowClosingListener(mainFrame);

        final Container loginDataPanel = new JPanel();

        final GroupLayout groupLayout = new GroupLayout(loginDataPanel);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        final Component usernameLabel = new JLabel("Nazwa użytkownika: ");
        final Component passwordLabel = new JLabel("Hasło: ");
        final Component usernameField = new JTextField("", 20);
        final Component passwordField = new JPasswordField("", 20);

        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(usernameLabel).addComponent(passwordLabel))
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(usernameField).addComponent(passwordField)));

        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGroup(createBaselineGroup(groupLayout)
                        .addComponent(usernameLabel).addComponent(usernameField))
                .addGroup(createBaselineGroup(groupLayout)
                        .addComponent(passwordLabel).addComponent(passwordField)));

        loginDataPanel.setLayout(groupLayout);

        loginDialog.add(loginDataPanel, BorderLayout.NORTH);

        final JComponent loginButtonsPanel = new JPanel();

        final JButton loginButton = createJButton("Zaloguj sie", e -> {
            if (controller != null) {
                controller.login();
            } else {
                loginDialog.dispose();
                mainFrame.setVisible(true);
            }
        });

        loginButtonsPanel.add(loginButton);
        loginButtonsPanel.add(createJButton("Anuluj", e -> exit()));
        loginButtonsPanel.add(createJButton("Zarejestruj sie", e -> {
            if (controller != null) {
                controller.createAccout();
            } else {
                JOptionPane.showMessageDialog(loginDialog,
                        "Rejestracja przebiegla pomyslnie.",
                        "Rejestracja udana", JOptionPane.INFORMATION_MESSAGE);
            }
        }));
        loginButtonsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        loginDialog.add(loginButtonsPanel, BorderLayout.SOUTH);
        loginDialog.getRootPane().setDefaultButton(loginButton);
        loginDialog.pack();
        loginDialog.setLocation((SCREEN_WIDTH - loginDialog.getWidth()) / 2,
                (SCREEN_HEIGHT - loginDialog.getHeight()) / 2);
        loginDialog.setResizable(false);
        loginDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowClosingListener(loginDialog);
    }

    @Override
    public final void init() {
        loginDialog.setVisible(true);
    }

    @Override
    public final void close() {
        mainFrame.dispose();
    }

    @Override
    public final void showConnectionError() {
        JOptionPane.showMessageDialog(loginDialog, "Nie udało się polączyć z serwerem!",
                "Blad polaczenia!", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public final void setController(final Controller controller) {
        this.controller = controller;
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
                exit();
            }
        });
    }

    private void exit() {
        if (controller != null) {
            controller.exit();
        } else {
            mainFrame.dispose();
        }
    }
}
