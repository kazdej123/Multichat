package client.view;

import client.controller.Controller;

public interface View {

    void init();

    void close();

    void setController(Controller controller);

    void showConnectionError();

    void showCreateAccountSuccess();

    void showCreateAccountError();
}