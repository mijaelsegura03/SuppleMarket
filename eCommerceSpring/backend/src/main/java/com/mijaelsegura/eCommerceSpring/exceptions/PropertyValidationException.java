package com.mijaelsegura.eCommerceSpring.exceptions;

public class PropertyValidationException extends RuntimeException{
    public PropertyValidationException(String message) {
        super(message);
    }
}
