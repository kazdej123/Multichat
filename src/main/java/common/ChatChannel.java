package common;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public final class ChatChannel {
    private final Socket socket;
    private final Scanner input;
    private final ChatWriter output;

    public ChatChannel(@NotNull final Socket socket) throws IOException {
        this.socket = socket;
        input = new Scanner(socket.getInputStream());
        output = new ChatWriter(socket.getOutputStream());
        // TODO
    }

    public final void writeln(final int i) throws IOException {
        output.writeln(i);
    }

    public final void close() throws IOException {
        try {
            if (output != null) {
                output.close();
            }
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } finally {
                if (socket != null) {
                    socket.close();
                }
            }
        }
    }

    public final int nextInt() {
        return input.nextInt();
    }

    public static Object next(final Object object) {
        // TODO
        return null;
    }

    private static final class ChatWriter extends BufferedWriter {
        private ChatWriter(final OutputStream outputStream) {
            super(new OutputStreamWriter(outputStream));
        }

        private final synchronized void writeln(final int i) throws IOException {
            final String s = String.valueOf(i);
            write(s, 0, s.length());
            newLine();
            flush();
        }
    }
}
