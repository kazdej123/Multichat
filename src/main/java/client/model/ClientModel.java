package client.model;

import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public final class ClientModel implements Model {
    private static final PrintStream err = System.err;

    private Socket socket = null;
    private Closeable scanner = null;
    private Object writer = null;

    public ClientModel() {
        // TODO
    }

    @Override
    public final void init() {
        err.println("Laczenie z serwerem...");
        try {
            socket = new Socket((String) null, 8189);
            try {
                scanner = new Scanner(socket.getInputStream());
                try {
                    writer = new PrintWriter(socket.getOutputStream(), false);
                    err.println("Pomyslnie polaczono z serwerem.");
                } catch (final IOException e) {
                    printConnectToServerError(e);
                    scanner.close();
                    closeSocketAndExit();
                }
            } catch (final IOException e) {
                printConnectToServerError(e);
                closeSocketAndExit();
            }
        } catch (final IOException e) {
            printConnectToServerError(e);
            System.exit(1);
        }
        // TODO
    }

    @Override
    public final void exit() {
        // TODO
    }

    private static void printConnectToServerError(final Throwable e) {
        printError(e, "Nie udalo sie polaczyc z serwerem");
    }

    private static void printError(@NotNull final Throwable e, final Object errorMessage) {
        err.println("BLAD! " + errorMessage);
        e.printStackTrace();
    }

    private void closeSocketAndExit() {
        try {
            socket.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        System.exit(1);
    }
}
