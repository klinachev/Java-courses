package info.kgeorgiy.ja.klinachev.walk;

import java.io.IOException;

public class WalkException extends Exception {
    public WalkException(String message) {
        super(message);
    }

    public WalkException(String message, IOException e) {
        super(message + e.getMessage(), e);
    }
}
