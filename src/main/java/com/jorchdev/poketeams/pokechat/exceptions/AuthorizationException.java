package com.jorchdev.poketeams.pokechat.exceptions;

import org.springframework.security.authorization.AuthorizationDeniedException;

public class AuthorizationException extends AuthorizationDeniedException {
    public AuthorizationException(String message) {
        super(message);
    }
}
