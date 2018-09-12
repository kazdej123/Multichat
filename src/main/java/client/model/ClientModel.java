package client.model;

import common.ChatChannel;

import java.io.IOException;
import java.net.Socket;

import static common.ChatUtilities.*;

public final class ClientModel implements Model {
    private ChatChannel channel = null;

    @Override
    public final void init() throws IOException {
        channel = new ChatChannel(new Socket((String) null, PORT));
    }

    @Override
    public final void close() throws IOException {
        if (channel != null) {
            try {
                println("Wysylanie do serwera zadania rozlaczenia...");
                channel.writeln(EXIT);
                println("Pomyslnie wyslano do serwera zadanie rozlaczenia.");
            } finally {
                channel.close();
            }
        }
    }
}
