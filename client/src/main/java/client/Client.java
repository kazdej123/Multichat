package client;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Pattern;

final class Client {
    private static final Scanner input = new Scanner(System.in);
    private static final PrintStream output = System.out;
    private static final PrintStream err = System.err;

    private Socket socket = null;
    private Scanner scanner = null;
    private PrintWriter writer = null;

    public static void main(final String[] args) {
        final Client client = new Client();
        client.login();
        client.exit();
    }

    private Client() {
        err.println("Laczenie z serwerem...");
        try {
            socket = new Socket((String) null, 8189);
            try {
                scanner = new Scanner(socket.getInputStream());
                try {
                    writer = new PrintWriter(socket.getOutputStream(), true);
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
    }

    private static void printConnectToServerError(final Exception e) {
        printError(e, "Nie udalo sie polaczyc z serwerem");
    }

    private void closeSocketAndExit() {
        try {
            socket.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        System.exit(1);
    }

    private void login() {
        String login;

        while (true) {
            output.print("Podaj login: ");
            if (Pattern.matches("[a-zA-Z_0-9]+", login = input.nextLine())) {
                break;
            }
            output.print("Niepoprawny login! ");
        }
        writer.println(login);

        if (scanner.nextBoolean()) {
            output.println("\n\n\n111111111111111111111111111111111");
        } else {
            output.println("\n\n\n00000000000000000000000000000000");
        }

        // TODO
    }

    private void exit() {
        err.println("Rozlaczanie z serwerem...");
        writer.close();
        scanner.close();
        try {
            socket.close();
            err.println("Pomyslnie rozlaczono sie z serwerem.");
        } catch (final IOException e) {
            printError(e, "Nie udalo sie rozlaczyc z serwerem.");
        }
    }

    private static void printError(@NotNull final Exception e, final Object errorMessage) {
        err.println("BLAD! " + errorMessage);
        e.printStackTrace();
    }
}