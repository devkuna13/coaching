package com.saas.coaching.exception;

public class InvalidFeePaymentException extends RuntimeException {

    public InvalidFeePaymentException(String message) {
        super(message);
    }
}
