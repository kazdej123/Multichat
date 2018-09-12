package server;

import common.ChatChannel;

import java.io.IOException;
import java.net.Socket;

import static common.ChatUtilities.*;

final class ClientHandler implements Runnable {
    private ChatChannel channel = null;
    private boolean running = false;

    ClientHandler(final Socket socket) {
        if (socket != null) {
            try {
                println("Tworzenie polaczenia z klientem...");
                channel = new ChatChannel(socket);
                running = true;
                println("Pomyslnie utworzono polaczenie z klientem.");
            } catch (final IOException e) {
                printError(e, "Nie udalo sie polaczyc z klientem.");
                close();
            }
        }
    }

    @Override
    public final void run() {
        while (running) {
            switch (channel.nextInt()) {
                case EXIT: exit(); return;
                default: break;
            }
        }
    }

    /*private void login() {
        final Object login = channel.next("[a-zA-Z_0-9]+");
        System.out.println(login);
    }*/

    private void exit() {
        close();
        // TODO
    }

    private void close() {
        try {
            println("Zamykanie polaczenia z klientem...");
            channel.close();
            println("Pomyslnie zamknieto polaczenie z klientem.");
        } catch (final IOException e) {
            printError(e, "Nie udalo sie zamknac polaczenia z klientem.");
        } finally {
            running = false;
        }
    }
}
