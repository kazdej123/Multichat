package client.controller;

import client.model.Model;
import client.view.View;

public final class ClientController implements Controller {
    private final Model model;
    private final View view;

    public ClientController(final Model model, final View view) {
        this.model = model;
        this.view = view;
        // TODO
    }

    @Override
    public final void start() {
        model.init();
        view.init();
        // TODO
    }

    @Override
    public final void exit() {
        // TODO
    }

    @Override
    public final void login() {
        // TODO
    }
}
