package com.mijaelsegura.eCommerceSpring.exceptions;

public class ResourceExistingException extends RuntimeException {
    public ResourceExistingException(String message) {
        super(message);
    }
}
