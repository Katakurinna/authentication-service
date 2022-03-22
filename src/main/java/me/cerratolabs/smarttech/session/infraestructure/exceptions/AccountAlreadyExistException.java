package me.cerratolabs.smarttech.session.infraestructure.exceptions;

import javax.security.auth.login.AccountException;

public class AccountAlreadyExistException extends AccountException {

    public AccountAlreadyExistException() {
        super();
    }

    public AccountAlreadyExistException(String msg) {
        super(msg);
    }

}