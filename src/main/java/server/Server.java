package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static common.ChatUtilities.printError;
import static common.ChatUtilities.println;

final class Server implements Runnable {
    private enum ServerExitMode {
        SHUTDOWN, SHUTDOWN_NOW, EXIT
    }

    private ServerSocket serverSocket = null;
    private Thread serverMainThread = null;
    private ExecutorService executorService = null;

    public static void main(final String[] args) {
        final Server server = new Server();

        while (true) {
            switch ((new Scanner(System.in)).nextLine()) {
                case "shutdown":
                    server.exit(ServerExitMode.SHUTDOWN);
                    return;
                case "shutdown -n":
                    server.exit(ServerExitMode.SHUTDOWN_NOW);
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
        try {
            println("Uruchamianie serwera...");
            serverSocket = new ServerSocket(8189);
            serverMainThread = new Thread(this);
            executorService = Executors.newFixedThreadPool(5);

            serverMainThread.start();
        } catch (final IOException e) {
            printError(e, "Nie udalo sie uruchomic serwera.");
            System.exit(-1);
        }
    }

    private void exit(final ServerExitMode serverExitMode) {
        try {
            println("Zamykanie gniazda serwera...");
            serverSocket.close();
        } catch (final IOException e) {
            printError(e,"Nie udalo sie zamknac gniazda serwera.");
        } finally {
            switch (serverExitMode) {
                case SHUTDOWN:
                    executorService.shutdown();
                    break;
                case SHUTDOWN_NOW:
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
                println("Oczekiwanie na watek akceptujacy polaczenia...");
                serverMainThread.join();
            } catch (final InterruptedException e1) {
                printError(e1,"Watek akceptujacy polaczenia zostal przerwany.");
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
                println("Przerwano dzialanie serwera.");
                return;
            } catch (final IOException e) {
                printError(e, "Nie udalo sie polaczyc z nowym klientem.");
            }
        }
    }
}
