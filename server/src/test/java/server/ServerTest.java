package server;

import org.junit.Test;

import java.io.IOException;

public final class ServerTest {
    @Test
    public final void shouldStartMainMethod() throws IOException {
        Server.main(null);
    }
}
