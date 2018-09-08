package server;

import java.io.IOException;
import java.net.Socket;

final class ClientHandler implements Runnable {
    private final Socket socket;

    ClientHandler(final Socket socket) {
        this.socket = socket;
    }

    @Override
    public final void run() {
        // TODO
        println("Zamykam polaczenie z klientem...");
        try {
            socket.close();
            println("Klient rozlaczyl sie pomyslnie.");
        } catch (final IOException e) {
            println("BLAD! Nie udalo sie rozlaczyc klienta.");
            e.printStackTrace();
        }
    }

    private static void println(final Object object) {
        Server.println(object);
    }
}
