package client.main;

import client.controller.ClientController;
import client.controller.Controller;
import client.model.ClientModel;
import client.model.Model;
import client.view.ClientView;
import client.view.View;

import javax.swing.*;
import java.awt.*;

final class Client {
    public static void main(final String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            EventQueue.invokeLater(() -> {
                final Model model = new ClientModel();
                final View view = new ClientView();
                final Controller controller = new ClientController(model, view);
                view.setController(controller);
                controller.init();
            });
        } catch (final Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
