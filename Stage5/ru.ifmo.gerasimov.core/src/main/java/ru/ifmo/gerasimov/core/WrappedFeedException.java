package ru.ifmo.gerasimov.core;

/**
 * @author Michael Gerasimov
 */
public class WrappedFeedException extends Exception {
    public WrappedFeedException() {
        super();
    }

    public WrappedFeedException(String message) {
        super(message);
    }

    public WrappedFeedException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrappedFeedException(Throwable cause) {
        super(cause);
    }

    public WrappedFeedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
