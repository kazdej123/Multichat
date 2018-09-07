package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

final class Client {
    private static final int PORT = 8189;

    public static void main(final String[] args) throws IOException {
        new Client();
    }

    private Client() throws IOException {
        try (final Socket socket = new Socket((String) null, PORT)) {
            try (final Scanner input = new Scanner(socket.getInputStream());
                 final PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {
                output.println("Witam, tu klient.");
                System.out.println(input.nextLine());
            }
        }
    }
}