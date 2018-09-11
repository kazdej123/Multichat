package server;

import common.ChatReader;
import common.ChatWriter;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

import static common.ChatUtilities.printError;
import static common.ChatUtilities.println;

final class ClientHandler implements Runnable {
    private static final int EXIT = 0;
    private static final int LOGIN = 1;

    private final Closeable socket;

    private ChatReader chatReader = null;
    private ChatWriter chatWriter = null;
    private boolean running = false;

    ClientHandler(final Socket socket) {
        this.socket = socket;

        if (socket != null) {
            try {
                println("Tworzenie strumienia do odczytu...");
                chatReader = new ChatReader(socket.getInputStream());
                try {
                    println("Tworzenie strumienia do zapisu...");
                    chatWriter = new ChatWriter(socket.getOutputStream());
                } catch (final IOException e) {
                    printError(e, "Nie udalo sie utworzyc strumienia do zapisu.");
                    chatReader.close();
                    closeSocket();
                }
            } catch (final IOException e) {
                printError(e, "Nie udalo sie utworzyc strumienia do odczytu.");
                closeSocket();
            }
            running = true;
        }
    }

    private void closeSocket() {
        try {
            println("Zamykanie polaczenia...");
            socket.close();
        } catch (final IOException e) {
            printError(e, "Nie udalo sie zamknac polaczenia.");
        }
    }

    @Override
    public final void run() {
        if (socket != null) {
            while (running) {
                switch (chatReader.nextInt()) {
                    case LOGIN: login(); break;
                    case EXIT: exit(); return;
                    default: break;
                }
            }
        }
    }

    private void login() {
        final Object login = chatReader.next("[a-zA-Z_0-9]+");
        System.out.println(login);
    }

    private void exit() {
        try {
            println("Zamykanie polaczenia z klientem...");
            close();
        } catch (final IOException e) {
            printError(e, "Nie udalo sie zamknac polaczenia z klientem.");
        } finally {
            running = false;
        }
        // TODO
    }

    private void close() throws IOException {
        try {
            if (chatWriter != null) {
                println("Zamykanie strumienia do zapisu...");
                chatWriter.close();
            }
        } finally {
            try {
                if (chatReader != null) {
                    println("Zamykanie strumienia do odczytu...");
                    chatReader.close();
                }
            } finally {
                if (socket != null) {
                    println("Zamykanie polaczenie...");
                    socket.close();
                }
            }
        }
    }
}
