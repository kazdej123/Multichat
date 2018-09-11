package client.controller;

import client.model.Model;
import client.view.View;

import java.io.IOException;

import static common.ChatUtilities.printError;
import static common.ChatUtilities.println;

public final class ClientController implements Controller {
    private final Model model;
    private final View view;

    public ClientController(final Model model, final View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public final void init() {
        if (model != null) {
            try {
                println("Laczenie z serwerem...");
                model.init();
            } catch (final IOException e) {
                printError(e, "Nie udalo sie polaczyc z serwerem.");
                view.showConnectionError();
                closeModel();
                System.exit(-1);
            }
        }
        if (view != null) {
            view.init();
        }
    }

    private void closeModel() {
        try {
            println("Rozlaczanie z serwerem...");
            model.close();
        } catch (final IOException e1) {
            printError(e1, "Nie udalo sie rozlaczyc z serwerem.");
            System.exit(-1);
        }
    }

    @Override
    public final void exit() {
        closeModel();
        view.close();
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
