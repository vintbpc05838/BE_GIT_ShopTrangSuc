package com.poly.shoptrangsuc.Exceptions;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String message) {
        super(message);  // Pass the message to the parent RuntimeException class
    }
}
