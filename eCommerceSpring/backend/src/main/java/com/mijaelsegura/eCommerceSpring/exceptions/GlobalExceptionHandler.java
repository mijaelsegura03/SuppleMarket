package com.mijaelsegura.eCommerceSpring.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    private ResponseEntity<String> ResourceNotFoundExceptionHandler(ResourceNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PropertyValidationException.class)
    private ResponseEntity<String> PropertyValidationExceptionHandler(PropertyValidationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ImageException.class)
    private ResponseEntity<String> ImageExceptionHandler(ImageException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserExistingException.class, ResourceExistingException.class})
    private ResponseEntity<String> ExistingUserExceptionHandler(RuntimeException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    private ResponseEntity<String> BadCredentialsExceptionHandler(BadCredentialsException e) {
        return new ResponseEntity<>("Invalid DNI or password.", HttpStatus.BAD_REQUEST);
    }


}
