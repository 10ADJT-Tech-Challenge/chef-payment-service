package com.adjt.application.exceptions;

public abstract class FalhaPagamentoException extends RuntimeException {
    public FalhaPagamentoException(String message) {
        super(message);
    }
}
