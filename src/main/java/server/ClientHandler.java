package server;

import common.ChatChannel;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import static common.ChatUtilities.*;

final class ClientHandler implements Runnable {
    private final Map usersMap;

    private ChatChannel channel = null;

    private boolean running = false;

    ClientHandler(@NotNull final Map usersMap, @NotNull final Socket socket) {
        this.usersMap = usersMap;
        try {
            println("Tworzenie polaczenia z klientem...");
            channel = new ChatChannel(socket);
            running = true;
            println("Pomyslnie utworzono polaczenie z klientem.");
        } catch (final IOException e) {
            printError(e, "Nie udalo sie polaczyc z klientem.");
            close();
        }
    }

    @Override
    public final void run() {
        if (channel != null) {
            while (running) {
                try {
                    switch (channel.nextInt()) {
                        case EXIT:
                            exit();
                            return;
                        case CREATE_ACCOUNT:
                            createAccount();
                            break;
                        default:
                            break;
                    }
                } catch (final IOException e) {
                    printError(e, "Blad wejscia / wyjscia.");
                    exit();
                }
            }
        }
    }

    private void exit() {
        close();
        // TODO
    }

    private void close() {
        try {
            println("Zamykanie polaczenia z klientem...");
            if (channel != null) {
                channel.close();
            }
            println("Pomyslnie zamknieto polaczenie z klientem.");
        } catch (final IOException e) {
            printError(e, "Nie udalo sie zamknac polaczenia z klientem.");
        } finally {
            running = false;
        }
    }

    private void createAccount() throws IOException {
        if (channel != null) {
            synchronized (usersMap) {
                if (usersMap.containsKey(channel.next())) {
                    while (true) {
                        try {
                            channel.writeMessage(USERNAME_ALREADY_TAKEN_ERROR);
                            break;
                        } catch (final IOException e) {
                            printError(e, "Nie udalo sie wyslac wiadomosci do klienta.");
                        }
                    }
                }
            }
        }
    }

    /*private void login() {
        final Object login = channel.next("[a-zA-Z_0-9]+");
        System.out.println(login);
    }*/
}
