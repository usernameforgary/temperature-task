package org.example.exception;

public class NeedRetryException extends Exception{
    /**
     * need retry when handling this exception
      * @param message
     */
    public NeedRetryException(String message) {
        super("NeedRetryException. " + message);
    }
}
