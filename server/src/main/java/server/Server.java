package server;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

final class Server implements Runnable {
    private static final PrintStream stderr = System.err;

    private enum ServerExitMode {
        SHUTDOWN, SHUTDOWN_NOW, EXIT
    }

    private final ServerSocket serverSocket;
    private final Thread serverMainThread;
    private final ExecutorService executorService;

    public static void main(final String[] args) throws IOException {
        final Server server = new Server();

        while (true) {
            synchronized (stderr) {
                stderr.print("> ");
            }
            switch ((new Scanner(System.in)).nextLine()) {
                case "shutdown": server.exit(ServerExitMode.SHUTDOWN); return;
                case "shutdown -n": server.exit(ServerExitMode.SHUTDOWN_NOW); return;
                case "exit": server.exit(ServerExitMode.EXIT); return;
                default: break;
            }
        }
    }

    private Server() throws IOException {
        println("Uruchamiam serwer...");
        serverSocket = new ServerSocket(8189);
        serverMainThread = new Thread(this);
        executorService = Executors.newFixedThreadPool(2);

        serverMainThread.start();
        println("Pomyslnie uruchomiono serwer.");
    }

    private void exit(final ServerExitMode serverExitMode) {
        println("Zamykam serwer...");
        try {
            serverSocket.close();
            shutdownExecutorService(serverExitMode);
            try {
                serverMainThread.join();
                println("Pomyslnie zamknieto serwer.");
            } catch (final InterruptedException e) {
                handleServerClosingError(e);
                System.exit(1);
            }
        } catch (final IOException e) {
            handleServerClosingError(e);
            shutdownExecutorService(serverExitMode);
            try {
                serverMainThread.join();
            } catch (final InterruptedException e1) {
                e1.printStackTrace();
            }
            System.exit(1);
        }
    }

    private void shutdownExecutorService(@NotNull final ServerExitMode serverExitMode) {
        switch (serverExitMode) {
            case SHUTDOWN: executorService.shutdown(); break;
            case SHUTDOWN_NOW: executorService.shutdownNow(); break;
            case EXIT: executorService.shutdownNow(); System.exit(1);
            default: break;
        }
        //noinspection StatementWithEmptyBody
        while (!executorService.isTerminated()) {}
    }

    private static void handleServerClosingError(@NotNull final Exception e) {
        println("BLAD! Nie udalo sie zamknac serwera.");
        e.printStackTrace();
    }

    @Override
    public final void run() {
        while (true) {
            println("Oczekiwanie na klienta...");
            try {
                final Socket socket = serverSocket.accept();
                executorService.execute(new ClientHandler(socket));
            } catch (final SocketException e) {
                println("Przerwano dzialanie gniazda serwera.");
                return;
            } catch (final IOException e) {
                println("BLAD! Polaczenie z nowym klientem nie powiodlo sie.");
                e.printStackTrace();
                return;
            }
        }
    }

    static void println(final Object object) {
        synchronized (stderr) {
            stderr.println(object);
        }
    }
}
