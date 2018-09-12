package client.model;

import common.ChatChannel;

import java.io.IOException;
import java.net.Socket;

import static common.ChatUtilities.EXIT;
import static common.ChatUtilities.PORT;

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
                channel.writeln(EXIT);
            } finally {
                channel.close();
            }
        }
    }
}
