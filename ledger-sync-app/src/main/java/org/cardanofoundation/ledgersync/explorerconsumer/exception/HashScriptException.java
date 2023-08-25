package org.cardanofoundation.ledgersync.explorerconsumer.exception;

public class HashScriptException extends RuntimeException {
    public HashScriptException(Exception e) {
        super(e);
    }

    public HashScriptException(String msg) {
        super(msg);
    }
}
