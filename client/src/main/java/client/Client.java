package client;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

final class Client {
    private static final String HOST = null;
    private static final int PORT = 8189;

    private static final Scanner stdin = new Scanner(System.in);
    private static final PrintStream stdout = System.out;

    public static void main(final String[] args) throws IOException {
        new Client();
    }

    private Client() throws IOException {
        write("Laczenie z serwerem...");
        try (final Socket socket = new Socket(HOST, PORT)) {
            try (final Scanner socketInput = new Scanner(socket.getInputStream());
                 final PrintWriter socketOutput = new PrintWriter(socket.getOutputStream(), true)) {
                write("Polaczono z serwerem.");

                while (true) {
                    stdout.print("Klient: ");
                    final String message = stdin.nextLine();
                    socketOutput.println(message);

                    if (message.equalsIgnoreCase("exit")) {
                        break;
                    }

                    stdout.print("Serwer: ");
                    final String inputMessage = socketInput.nextLine();
                    stdout.println(inputMessage);

                    if (inputMessage.equalsIgnoreCase("exit")) {
                        break;
                    }
                }
            }
        }
    }

    private static void write(final Object message) {
        System.err.println("Klient: " + message);
    }
}