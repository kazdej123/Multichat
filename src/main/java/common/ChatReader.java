package common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

final class ChatReader extends BufferedReader {
    private ChatReader(final InputStream inputStream) {
        super(new InputStreamReader(inputStream));
        // TODO
    }
}
