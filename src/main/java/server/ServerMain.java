package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static common.ChatUtilities.*;

final class ServerMain implements Runnable {
    private enum ServerExitMode {
        CLOSE, CLOSE_NOW, EXIT
    }

    private ServerSocket serverSocket = null;
    private Thread serverMainThread = null;
    private ExecutorService executorService = null;

    public static void main(final String[] args) {
        final ServerMain server = new ServerMain();

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

    private ServerMain() {
        synchronized (stderr) {
            try {
                println("Uruchamianie serwera...");
                serverSocket = new ServerSocket(PORT);
                serverMainThread = new Thread(this);
                executorService = Executors.newFixedThreadPool(5);

                serverMainThread.start();
                println("Pomyślnie uruchomiono serwer.");
            } catch (final IOException e) {
                printError(e, "Nie udało się uruchomić serwera.");
                System.exit(-1);
            }
        }
    }

    private void exit(final ServerExitMode serverExitMode) {
        try {
            println("Zamykanie serwera...");
            serverSocket.close();
        } catch (final IOException e) {
            printError(e,"Nie udało się zamknąć gniazda serwera.");
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
                println("Kończenie nasłuchiwania klientów...");
                serverMainThread.join();
                println("Pomyślnie zakończono nasłuchiwanie klientów.");
                println("Pomyślnie zamknięto serwer.");
            } catch (final InterruptedException e1) {
                printError(e1,"Nie udało się zakończyć nasłuchiwania klientów.");
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
            } catch (final SocketException e) {
                return;
            } catch (final IOException e) {
                printError(e, "Nie udało się połączyć z nowym klientem.");
            }
        }
    }
}
