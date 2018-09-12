package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static common.ChatUtilities.*;

final class Server implements Runnable {
    private enum ServerExitMode {
        CLOSE, CLOSE_NOW, EXIT
    }

    private ServerSocket serverSocket = null;
    private Thread serverMainThread = null;
    private ExecutorService executorService = null;

    public static void main(final String[] args) {
        final Server server = new Server();

        while (true) {
            switch ((new Scanner(System.in)).nextLine()) {
                case "close":
                    server.exit(ServerExitMode.CLOSE);
                    return;
                case "close -n":
                    server.exit(ServerExitMode.CLOSE_NOW);
                    return;
                case "exit":
                    server.exit(ServerExitMode.EXIT);
                    return;
                default:
                    break;
            }
        }
    }

    private Server() {
        synchronized (stderr) {
            try {
                println("Uruchamianie serwera...");
                serverSocket = new ServerSocket(PORT);
                serverMainThread = new Thread(this);
                executorService = Executors.newFixedThreadPool(5);
                serverMainThread.start();
                println("Pomyslnie uruchomiono serwer.");
            } catch (final IOException e) {
                printError(e, "Nie udalo sie uruchomic serwera.");
                System.exit(-1);
            }
        }
    }

    private void exit(final ServerExitMode serverExitMode) {
        try {
            println("Zamykanie serwera...");
            serverSocket.close();
        } catch (final IOException e) {
            printError(e,"Nie udalo sie zamknac gniazda serwera.");
        } finally {
            switch (serverExitMode) {
                case CLOSE:
                    executorService.shutdown();
                    break;
                case CLOSE_NOW:
                    executorService.shutdownNow();
                    break;
                case EXIT:
                    executorService.shutdownNow();
                    System.exit(1);
                default:
                    break;
            }
            //noinspection StatementWithEmptyBody
            while (!executorService.isTerminated()) {}
            try {
                println("Konczenie nasluchiwania klientow...");
                serverMainThread.join();
                println("Pomyslnie zakonczono nasluchiwanie klientow.");
                println("Pomyslnie zamknieto serwer.");
            } catch (final InterruptedException e1) {
                printError(e1,"Nie udalo sie zakonczyc nasluchiwania klientow.");
                System.exit(-1);
            }
        }
    }

    @Override
    public final void run() {
        while (true) {
            try {
                println("Oczekiwanie na klienta...");
                executorService.execute(new ClientHandler(serverSocket.accept()));
                println("Nowy klient przyszedl.");
            } catch (final SocketException e) {
                println("Przerwano oczekiwanie na nowych klientow.");
                return;
            } catch (final IOException e) {
                printError(e, "Nie udalo sie polaczyc z nowym klientem.");
            }
        }
    }
}
