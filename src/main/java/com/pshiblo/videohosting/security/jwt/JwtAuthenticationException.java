package com.pshiblo.videohosting.security.jwt;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Максим Пшибло
 */
public class JwtAuthenticationException extends AuthenticationException {
    public JwtAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public JwtAuthenticationException(String msg) {
        super(msg);
    }
}
