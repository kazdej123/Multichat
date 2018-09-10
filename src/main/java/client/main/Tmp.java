package client.main;

import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Pattern;

final class Tmp {
    private static final int EXIT = 0;
    private static final int LOGIN = 1;

    private static final Scanner input = new Scanner(System.in);
    private static final PrintStream output = System.out;
    private static final PrintStream err = System.err;

    private Closeable socket = null;
    private Scanner socketScanner = null;
    private PrintWriter socketPrintWriter = null;

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
        CharSequence login;
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
        socketPrintWriter.println(messageType);
        for (final Object messagePart : message) {
            socketPrintWriter.println(messagePart);
        }
        socketPrintWriter.flush();
    }

    private void exit() {
        err.println("Rozlaczanie z serwerem...");
        writeMessage(EXIT);
        socketPrintWriter.close();
        socketScanner.close();
        try {
            socket.close();
            err.println("Pomyslnie rozlaczono sie z serwerem.");
        } catch (final IOException e) {
            printError(e, "Nie udalo sie rozlaczyc z serwerem.");
        }
    }

    private static void printError(@NotNull final Throwable e, final Object errorMessage) {
        err.println("BLAD! " + errorMessage);
        e.printStackTrace();
    }
}