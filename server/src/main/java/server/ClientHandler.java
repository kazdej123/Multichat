package server;

import java.io.IOException;
import java.net.Socket;

final class ClientHandler implements Runnable {
    private static int clientCounter = 1;

    private final int clientNumber = clientCounter++;
    private final Socket socket;

    ClientHandler(final Socket socket) {
        this.socket = socket;
        println("Klient nr " + clientNumber + " nawiazal polaczenie.");
    }

    @Override
    public final void run() {
        try {
            socket.close();
            println("Klient nr " + clientNumber + " rozlaczyl sie.");
        } catch (IOException e) {
            println("BLAD! rozlaczania klienta nr " + clientNumber + ".");
            e.printStackTrace();
        }
    }

    private static void println(final Object object) {
        Server.println(object);
    }
}
