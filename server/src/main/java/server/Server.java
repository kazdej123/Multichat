package server;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

final class Server implements Runnable {
    private enum ServerState {
        RUNNING, SHUTDOWN, SHUTDOWN_NOW
    }

    private static final Scanner stdin = new Scanner(System.in);
    private static final PrintStream stderr = System.err;

    private final ServerSocket serverSocket = new ServerSocket(8189);
    private final Thread serverMainThread = new Thread(this);

    private volatile ServerState serverState;

    public static void main(final String[] args) throws IOException {
        final Server server = new Server();

        while (server.isRunning()) {
            System.out.print("> ");

            switch (stdin.nextLine()) {
                case "shutdown": server.exit(ServerState.SHUTDOWN); break;
                case "shutdown -n": server.exit(ServerState.SHUTDOWN_NOW); break;
                case "exit": System.exit(1);
                default: break;
            }
        }
    }

    private Server() throws IOException {
        println("Uruchamiam serwer...");
        serverState = ServerState.RUNNING;
        serverMainThread.start();
        println("Pomyslnie uruchomiono serwer.");
    }

    private void exit(final ServerState serverState) {
        println("Zamykam serwer...");

        synchronized (this) {
            this.serverState = serverState;
        }
        try {
            serverMainThread.join();
            try {
                serverSocket.close();
            } catch (final IOException e) {
                handleServerClosingError(e);
                System.exit(1);
            }
            println("Pomyslnie zamknieto serwer.");
        } catch (final InterruptedException e) {
            handleServerClosingError(e);
            try {
                serverSocket.close();
            } catch (final IOException e1) {
                e1.printStackTrace();
            }
            System.exit(1);
        }
    }

    private static void handleServerClosingError(@NotNull final Exception e) {
        println("BLAD! Nie udalo sie zamknac serwera.");
        e.printStackTrace();
    }

    @Override
    public final void run() {
        final ExecutorService executorService = Executors.newFixedThreadPool(2);

        while (isRunning()) {
            try {
                println("Oczekiwanie na klienta...");
                executorService.execute(new ClientHandler(serverSocket.accept()));
            } catch (final IOException e) {
                e.printStackTrace();
                executorService.shutdown();
            }
        }
        switch (serverState) {
            case SHUTDOWN: executorService.shutdown(); break;
            case SHUTDOWN_NOW: executorService.shutdownNow(); break;
            default: return;
        }
        //noinspection StatementWithEmptyBody
        while (!executorService.isTerminated()) {}
    }

    @Contract(pure = true)
    private synchronized boolean isRunning() {
        return serverState == ServerState.RUNNING;
    }

    static void println(final Object object) {
        synchronized (stderr) {
            stderr.println(object);
        }
    }
}
