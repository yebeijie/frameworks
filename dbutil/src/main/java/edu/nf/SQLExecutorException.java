package edu.nf;

public class SQLExecutorException extends RuntimeException {

    public SQLExecutorException(String message) {
        super(message);
    }

    public SQLExecutorException(String message, Throwable cause) {
        super(message, cause);
    }

    public SQLExecutorException(Throwable cause) {
        super(cause);
    }
}
