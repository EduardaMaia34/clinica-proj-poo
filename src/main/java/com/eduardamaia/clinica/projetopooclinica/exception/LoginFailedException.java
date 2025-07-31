package com.eduardamaia.clinica.projetopooclinica.exception;

public class LoginFailedException extends Exception {

    public LoginFailedException(String message) {
        super(message);
    }

    public LoginFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}