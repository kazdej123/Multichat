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
                println("Tworzenie połączenia z klientem...");
                channel = new ChatChannel(socket);
                running = true;
                println("Pomyślnie utworzono połączenie z klientem.");
            } catch (final IOException e) {
                printError(e, "Nie udało się połączyć z klientem.");
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

    private void exit() {
        close();
        // TODO
    }

    private void close() {
        try {
            println("Zamykanie połączenia z klientem...");
            channel.close();
            println("Pomyślnie zamknięto połączenie z klientem.");
        } catch (final IOException e) {
            printError(e, "Nie udało się zamknąć połączenia z klientem.");
        } finally {
            running = false;
        }
    }

    /*private void login() {
        final Object login = channel.next("[a-zA-Z_0-9]+");
        System.out.println(login);
    }*/
}
