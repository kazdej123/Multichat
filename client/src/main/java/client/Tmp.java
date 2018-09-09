package client;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Pattern;

final class Tmp {
    private static final int EXIT = 0;
    private static final int LOGIN = 1;

    private static final Scanner input = new Scanner(System.in);
    private static final PrintStream output = System.out;
    private static final PrintStream err = System.err;

    private Socket socket = null;
    private Scanner scanner = null;
    private PrintWriter writer = null;

    public static void main(final String[] args) {
        final Tmp client = new Tmp();
        client.init();
        client.exit();
    }

    private Tmp() {
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

    private void init() {
        while (true) {
            output.println("Co chcesz zrobic?");

            switch (input.nextInt()) {
                case LOGIN: login(); break;
                case EXIT: exit(); return;
                default: break;
            }
        }
    }

    private void login() {
        String login;
        input.nextLine();

        while (true) {
            output.print("Podaj login: ");
            if (Pattern.matches("[a-zA-Z_0-9]+", login = input.nextLine())) {
                break;
            }
            output.println("Niepoprawny login!:" + login);
        }
        output.println("Logowanie...");
        writeMessage(LOGIN, login);
    }

    private void writeMessage(final int messageType, @NotNull final Object... message) {
        writer.println(messageType);
        for (final Object messagePart : message) {
            writer.println(messagePart);
        }
        writer.flush();
    }

    private void exit() {
        err.println("Rozlaczanie z serwerem...");
        writeMessage(EXIT);
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