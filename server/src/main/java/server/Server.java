package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

final class Server {
    private static final int PORT = 8189;

    public static void main(final String[] args) throws IOException {
        write("Uruchamianie serwera...");
        try (final ServerSocket serverSocket = new ServerSocket(PORT)) {
            write("Pomyślnie uruchomiono serwer.\nCzekam na połączenie z klientem...");
            try (final Socket socket = serverSocket.accept()) {
                write("Połączono z klientem");
                try (final Scanner input = new Scanner(socket.getInputStream());
                     final PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {
                    output.println("Witam, tu serwer.");
                    System.out.println(input.nextLine());
                }
            }
        }
    }

    private static void write(final Object message) {
        System.err.println(message);
    }
}
