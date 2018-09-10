package server;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

final class Server implements Runnable {
    private enum ServerExitMode {
        SHUTDOWN, SHUTDOWN_NOW, EXIT
    }

    private ServerSocket serverSocket = null;

    private final Thread serverMainThread;
    private final ExecutorService executorService;

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
        println("Uruchamianie serwera...");
        try {
            serverSocket = new ServerSocket(8189);
        } catch (final IOException e) {
            printError(e, "Nie udalo sie uruchomic serwera.");
            System.exit(1);
        }
        serverMainThread = new Thread(this);
        executorService = Executors.newFixedThreadPool(5);

        serverMainThread.start();
        println("Pomyslnie uruchomiono serwer.");
    }

    private void exit(final ServerExitMode serverExitMode) {
        println("Zamykanie serwera...");
        try {
            serverSocket.close();
            shutdownExecutorService(serverExitMode);
            try {
                serverMainThread.join();
                println("Pomyslnie zamknieto serwer.");
            } catch (final InterruptedException e) {
                printServerExitError(e);
                System.exit(1);
            }
        } catch (final IOException e) {
            printServerExitError(e);
            shutdownExecutorService(serverExitMode);
            try {
                serverMainThread.join();
            } catch (final InterruptedException e1) {
                e1.printStackTrace();
            }
            System.exit(1);
        }
    }

    private void printServerExitError(final Throwable e) {
        printError(e,"Nie udalo sie zamknac serwera.");
    }

    private void shutdownExecutorService(@NotNull final ServerExitMode serverExitMode) {
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
    }

    @Override
    public final void run() {
        while (true) {
            println("Oczekiwanie na klienta...");
            try {
                executorService.execute(new ClientHandler(serverSocket.accept()));
            } catch (final SocketException e) {
                println("Przerwano dzialanie gniazda serwera.");
                return;
            } catch (final IOException e) {
                printError(e, "Nie udalo sie polaczyc z nowym klientem.");
            }
        }
    }

    static void printError(@NotNull final Throwable e, final Object errorMessage) {
        println("BLAD! " + errorMessage);
        e.printStackTrace();
    }

    static void println(final Object object) {
        synchronized (System.err) {
            System.err.println(object);
        }
    }
}
