package client.model;

import common.ChatChannel;

import java.io.IOException;
import java.net.Socket;

import static common.ChatUtilities.CREATE_ACCOUNT;
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
                channel.writeMessage(EXIT);
            } finally {
                channel.close();
            }
        } else {
            throw new IOException();
        }
    }

    @Override
    public final int createAccount(final Object username) throws IOException {
        if (channel != null) {
            channel.writeMessage(CREATE_ACCOUNT, username);
            return channel.nextInt();
        } else {
            throw new IOException();
        }
    }
}
