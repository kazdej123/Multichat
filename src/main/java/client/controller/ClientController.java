package client.controller;

import client.model.Model;
import client.view.View;

public final class ClientController implements Controller {
    private final Model model;
    private final View view;

    public ClientController(final Model model, final View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public final void start() {
        model.init();
        view.init();
    }

    @Override
    public final void exit() {
        model.exit();
        view.exit();
    }

    @Override
    public final void login() {
        // TODO
    }

    @Override
    public final void register() {
        // TODO
    }
}
