package me.cerratolabs.smarttech.session.domain.exceptions;

public class InternalSessionException extends Exception {

    public InternalSessionException() {
        super();
    }


    public InternalSessionException(String message) {
        super(message);
    }

    public InternalSessionException(String message, Throwable cause) {
        super(message, cause);
    }
}