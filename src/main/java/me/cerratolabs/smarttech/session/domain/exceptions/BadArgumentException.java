package me.cerratolabs.smarttech.session.domain.exceptions;

public class BadArgumentException extends Exception {

    public BadArgumentException() {
        super();
    }

    public BadArgumentException(String message) {
        super(message);
    }

}