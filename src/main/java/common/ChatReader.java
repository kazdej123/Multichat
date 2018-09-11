package common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class ChatReader extends BufferedReader {
    public ChatReader(final InputStream inputStream) {
        super(new InputStreamReader(inputStream));
        // TODO
    }

    public static final int nextInt() {
        return 0;
    }

    public static final Object next(final Object object) {
        return null;
    }
}
