package com.mijaelsegura.eCommerceSpring.exceptions;

public class UserExistingException extends RuntimeException {
    public UserExistingException(String message) {
        super(message);
    }
}
