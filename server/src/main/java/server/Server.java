package server;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

final class Server {
    private static final int PORT = 8189;

    public static void main(final String[] args) throws IOException {
        write("Starting the server...");
        try (final ServerSocket serverSocket = new ServerSocket(PORT)) {
            write("Server started.");

            write("Waiting for client...");
            try (final Socket socket = serverSocket.accept()) {
                write("Client connected.");

                try (final Scanner socketInput = new Scanner(socket.getInputStream());
                     final PrintWriter socketOutput = new PrintWriter(socket.getOutputStream(), true)) {

                    final Scanner input = new Scanner(System.in);
                    final PrintStream output = System.out;

                    while (socketInput.hasNextLine()) {
                        final String message = socketInput.nextLine();
                        output.println(message);

                        if (message.equalsIgnoreCase("exit")) {
                            break;
                        }
                    }
                }
            }
        }
    }

    private static void write(final Object message) {
        System.err.println("Server: " + message);
    }
}
