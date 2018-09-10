package common;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

final class ChatWriter extends BufferedWriter {
    private ChatWriter(final OutputStream outputStream) {
        super(new OutputStreamWriter(outputStream));
        // TODO
    }
}
