package com.dsi.demoappbackend.appuser.exceptions;

public class BadCredentialsException extends Exception {
    public BadCredentialsException(String errorMessage) {
        super(errorMessage);
    }
}
