package common;

import org.jetbrains.annotations.NotNull;

public final class ChatUtilities {
    public static final int PORT = 8189;

    public static final int EXIT = 0;

    public static void printError(@NotNull final Throwable e, final Object errorMessage) {
        System.err.println("BLAD! " + errorMessage);
        e.printStackTrace();
        System.err.println();
    }

    public static void println(final Object object) {
        synchronized (System.err) {
            System.err.println(object);
        }
    }
}
