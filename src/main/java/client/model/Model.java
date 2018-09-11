package client.model;

import java.io.IOException;

public interface Model {
    void init() throws IOException;

    void close() throws IOException;
}
