package server;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

final class Server {
    private static final int PORT = 8189;

    private static final Scanner stdin = new Scanner(System.in);
    private static final PrintStream stdout = System.out;

    public static void main(final String[] args) throws IOException {
        write("Uruchamiam serwer...");
        try (final ServerSocket serverSocket = new ServerSocket(PORT)) {
            write("Serwer zostal pomyslnie uruchomiony.");

            write("Oczekiwanie na klienta...");
            try (final Socket socket = serverSocket.accept()) {
                try (final Scanner socketInput = new Scanner(socket.getInputStream());
                     final PrintWriter socketOutput = new PrintWriter(socket.getOutputStream(), true)) {
                    write("Klient zostal polaczony.");

                    while (true) {
                        stdout.print("Klient: ");
                        final String inputMessage = socketInput.nextLine();
                        stdout.println(inputMessage);

                        if (inputMessage.equalsIgnoreCase("exit")) {
                            break;
                        }

                        stdout.print("Serwer: ");
                        final String outputMessage = stdin.nextLine();
                        socketOutput.println(outputMessage);

                        if (outputMessage.equalsIgnoreCase("exit")) {
                            break;
                        }
                    }
                }
            }
        }
    }

    private static void write(final Object message) {
        System.err.println("Serwer: " + message);
    }
}
