package client.model;

import common.ChatReader;
import common.ChatWriter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import static common.ChatUtilities.*;

public final class ClientModel implements Model {
    private Socket socket = null;
    private ChatReader chatReader = null;
    private ChatWriter chatWriter = null;

    @Override
    public final void init() throws IOException {
        socket = new Socket((String) null, 8189);
        chatReader = new ChatReader(socket.getInputStream());
        chatWriter = new ChatWriter(socket.getOutputStream());
    }

    @Override
    public final void close() throws IOException {
        try {
            if (chatWriter != null) {
                println("Wysylanie do serwera zadania rozlaczenia...");
                chatWriter.write(0);
            }
        } finally {
            try {
                if (chatWriter != null) {
                    println("Zamykanie strumienia do zapisu...");
                    chatWriter.close();
                }
            } finally {
                try {
                    if (chatReader != null) {
                        println("Zamykanie strumienia do odczytu...");
                        chatReader.close();
                    }
                } finally {
                    if (socket != null) {
                        println("Zamykanie polaczenie...");
                        socket.close();
                    }
                }
            }
        }
    }
}
