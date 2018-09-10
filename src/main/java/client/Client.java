package client;

import client.view.ClientView;

import javax.swing.*;
import java.awt.*;

final class Client {
    public static void main(final String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            EventQueue.invokeLater(ClientView::new);
        } catch (final Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
