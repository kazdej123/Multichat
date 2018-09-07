package client;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

final class Client {
    private static final String HOST = null;
    private static final int PORT = 8189;

    public static void main(final String[] args) throws IOException {
        new Client();
    }

    private Client() throws IOException {
        write("Connecting with server...");
        try (final Socket socket = new Socket(HOST, PORT)) {
            write("Connected with server.");

            try (final Scanner socketInput = new Scanner(socket.getInputStream());
                 final PrintWriter socketOutput = new PrintWriter(socket.getOutputStream(), true)) {

                final Scanner input = new Scanner(System.in);
                final PrintStream output = System.out;

                while (input.hasNextLine()) {
                    final String message = input.nextLine();
                    socketOutput.println(message);

                    if (message.equalsIgnoreCase("exit")) {
                        break;
                    }
                }
            }
        }
    }

    private static void write(final Object message) {
        System.err.println("Client: " + message);
    }
}