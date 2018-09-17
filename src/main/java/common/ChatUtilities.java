package common;

import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;

public final class ChatUtilities {
    public static final int PORT = 8189;

    public static final int EXIT = 0;
    public static final int CREATE_ACCOUNT = 1;

    public static final int USERNAME_ALREADY_TAKEN_ERROR = -1;

    public static final PrintStream stderr = System.err;

    public static void printError(@NotNull final Throwable e, final Object message) {
        stderr.println("BLAD! " + message);
        e.printStackTrace();
        stderr.println();
    }

    public static void println(final Object object) {
        stderr.println(object);
    }
}
