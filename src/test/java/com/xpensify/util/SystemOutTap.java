package com.xpensify.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class SystemOutTap {

    public static String tapSystemOut(PrintingCallable callable) {
        final PrintStream standardOut = System.out;
        try {
            final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outputStreamCaptor));

            callable.call();

            return outputStreamCaptor.toString();
        } finally {
            System.setOut(standardOut);
        }
    }

    @FunctionalInterface
    public interface PrintingCallable {
        void call();
    }
}
