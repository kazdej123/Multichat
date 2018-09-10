package client;

import controller.ClientController;
import controller.Controller;
import model.ClientModel;
import model.Model;
import view.ClientView;
import view.View;

import javax.swing.UIManager;
import java.awt.EventQueue;

final class Client {
    public static void main(final String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            EventQueue.invokeLater(() -> {
                final Model model = new ClientModel();
                final View view = new ClientView();
                final Controller controller = new ClientController(model, view);
                controller.start();
            });
        } catch (final Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
