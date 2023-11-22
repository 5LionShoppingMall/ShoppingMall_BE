package com.ll.lion.user.security;

public class NoTokenException extends RuntimeException{
    public NoTokenException(String message) {
        super(message);
    }
}
