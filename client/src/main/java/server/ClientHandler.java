package server;

import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

final class ClientHandler implements Runnable {
    private static final int EXIT = 0;
    private static final int LOGIN = 1;

    private final Closeable socket;

    private Scanner socketScanner = null;
    private PrintWriter socketPrintWriter = null;

    ClientHandler(final Socket socket) {
        println("Laczenie z klientem...");
        this.socket = socket;

        if (socket != null) {
            try {
                socketScanner = new Scanner(socket.getInputStream());
                try {
                    socketPrintWriter = new PrintWriter(socket.getOutputStream(), true);
                    println("Pomyslnie polaczono z klientem.");
                } catch (final IOException e) {
                    printConnectToClientError(e);
                    socketScanner.close();
                    closeSocket();
                }
            } catch (final IOException e) {
                printConnectToClientError(e);
                closeSocket();
            }
        }
    }

    private static void printConnectToClientError(final Throwable e) {
        printError(e, "Nie udalo sie polaczyc z klientem");
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public final void run() {
        if (socket != null) {
            while (true) {
                switch (socketScanner.nextInt()) {
                    case LOGIN: login(); break;
                    case EXIT: exit(); return;
                    default: break;
                }
            }
        }
    }

    private void login() {
        final Object login = socketScanner.next("[a-zA-Z_0-9]+");
        System.out.println(login);
    }

    private void exit() {
        println("Rozlaczanie z klientem...");
        socketPrintWriter.close();
        socketScanner.close();
        try {
            socket.close();
            println("Pomyslnie rozlaczono sie z klientem.");
        } catch (final IOException e) {
            printError(e, "Nie udalo sie rozlaczyc z klientem.");
        }
    }

    private static void println(final Object object) {
        Server.println(object);
    }

    private static void printError(@NotNull final Throwable e, final Object errorMessage) {
        Server.printError(e, errorMessage);
    }
}
