/*
package client.main;

import java.util.Scanner;

final class Tmp {
    private static final Scanner input = new Scanner(System.in);

    public Tmp() {
        input.nextInt();
    }
    private Closeable socket = null;
    private ChatReader chatReader = null;
    private ChatWriter chatWriter = null;

    private void init() {
        while (true) {
            output.println("Co chcesz zrobic?");

            switch (input.nextInt()) {
                case 0: login(); break;
                case 1: exit(); return;
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
        writeMessage(1, login);
    }

    private void writeMessage(final int messageType, @NotNull final Object... message) {
        chatWriter.println(messageType);
        for (final Object messagePart : message) {
            chatWriter.println(messagePart);
        }
        chatWriter.flush();
    }

    private void exit() {
        err.println("Rozlaczanie z serwerem...");
        writeMessage(0);
        chatWriter.close();
        chatReader.close();
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
}*/
