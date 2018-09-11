package common;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public final class ChatWriter extends BufferedWriter {
    public ChatWriter(final OutputStream outputStream) {
        super(new OutputStreamWriter(outputStream));
        // TODO
    }
}
