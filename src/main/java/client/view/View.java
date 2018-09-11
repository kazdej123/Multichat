package client.view;

import client.controller.Controller;

public interface View {
    void init();

    void close();

    void showConnectionError();

    void setController(Controller controller);
}