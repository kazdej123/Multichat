package server;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

final class ClientHandler implements Runnable {
    private final Socket socket;

    private Scanner scanner = null;
    private PrintWriter writer = null;

    ClientHandler(final Socket socket) {
        println("Laczenie z klientem...");
        this.socket = socket;

        if (socket != null) {
            try {
                scanner = new Scanner(socket.getInputStream());
                try {
                    writer = new PrintWriter(socket.getOutputStream(), true);
                    println("Pomyslnie polaczono z klientem.");
                } catch (final IOException e) {
                    printConnectToClientError(e);
                    scanner.close();
                    closeSocket();
                }
            } catch (final IOException e) {
                printConnectToClientError(e);
                closeSocket();
            }
        }
    }

    private static void printConnectToClientError(final Exception e) {
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
            final String login = scanner.next();
            System.out.println(login);
            if (login.equals("kano")) {
                writer.print(true);
            } else {
                writer.print(false);
            }
            exit();
        }
    }

    private void exit() {
        println("Rozlaczanie z klientem...");
        writer.close();
        scanner.close();
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

    private static void printError(@NotNull final Exception e, final Object errorMessage) {
        Server.printError(e, errorMessage);
    }
}
