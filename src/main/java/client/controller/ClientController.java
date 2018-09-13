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
                println("Łączenie z serwerem...");
                model.init();
                println("Pomyślnie połączono się z serwerem.");
            } catch (final IOException e) {
                printError(e, "Nie udało się połączyć z serwerem.");
                view.showConnectionError();
                closeModel();
                System.exit(-1);
            }
        }
        if (view != null) {
            view.init();
        }
    }

    @Override
    public final void exit() {
        closeModel();
        view.close();
    }

    private void closeModel() {
        try {
            println("Rozłączanie z serwerem...");
            model.close();
            println("Pomyślnie rozłączono się z serwerem.");
        } catch (final IOException e1) {
            printError(e1, "Nie udało się rozłączyć z serwerem.");
            System.exit(-1);
        }
    }

    @Override
    public final void createAccout() {
        // TODO
    }

    @Override
    public final void login() {
        // TODO
    }
}
