package common;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public final class ChatChannel {
    private final Closeable socket;
    private final Scanner input;
    private final ChatWriter output;

    public ChatChannel(@NotNull final Socket socket) throws IOException {
        this.socket = socket;
        input = new Scanner(socket.getInputStream());
        output = new ChatWriter(socket.getOutputStream());
    }

    public final void writeMessage(final int i, final Object... objects) throws IOException {
        if (output != null) {
            output.writeMessage(i, objects);
        } else {
            throw new IOException();
        }
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
                socket.close();
            }
        }
    }

    public final int nextInt() throws IOException {
        if (input != null) {
            return input.nextInt();
        } else {
            throw new IOException();
        }
    }

    public final Object next() throws IOException {
        if (input != null) {
            return input.next();
        } else {
            throw new IOException();
        }
    }

    private static final class ChatWriter extends BufferedWriter {
        private ChatWriter(final OutputStream outputStream) {
            super(new OutputStreamWriter(outputStream));
        }

        private synchronized void writeMessage(final int i, final Object[] objects) throws IOException {
            writeln(String.valueOf(i));
            flush();
        }

        private void writeln(final String s) throws IOException {
            write(s, 0, s.length());
            newLine();
        }
    }
}
