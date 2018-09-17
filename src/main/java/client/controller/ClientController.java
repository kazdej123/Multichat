package client.controller;

import client.model.Model;
import client.view.View;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static common.ChatUtilities.printError;
import static common.ChatUtilities.println;

public final class ClientController implements Controller {
    private final Model model;
    private final View view;

    public ClientController(@NotNull final Model model, @NotNull final View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public final void init() {
        try {
            println("Laczenie z serwerem...");
            model.init();
            println("Pomyslnie polaczono sie z serwerem.");
            view.init();
        } catch (final IOException e) {
            printError(e, "Nie udalo sie polaczyc z serwerem.");
            view.showConnectionError();
            closeModel();
            System.exit(-1);
        }
    }

    @Override
    public final void exit() {
        closeModel();
        view.close();
    }

    private void closeModel() {
        try {
            println("Rozlaczanie z serwerem...");
            model.close();
            println("Pomyslnie rozlaczono sie z serwerem.");
        } catch (final IOException e1) {
            printError(e1, "Nie udalo sie rozlaczyc z serwerem.");
            System.exit(-1);
        }
    }

    @Override
    public final void createAccout(final Object username) {
        try {
            println("Zakladam nowe konto...");
            model.createAccount(username);
            println("Rejestracja przebiegla pomyslnie.");
            view.showCreateAccountSuccess();
        } catch (final IOException e) {
            printError(e, "Rejestracja sie nie powiodla.");
            view.showCreateAccountError();
        }
    }

    @Override
    public final void login() {
        // TODO
    }
}
