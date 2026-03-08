package com.adjt.application.exceptions;

import java.util.UUID;

public class PagamentoNaoEncontradoException extends RuntimeException {
    public PagamentoNaoEncontradoException(UUID pagamentoId) {
        super("Pagamento não encontrado com id %s".formatted(pagamentoId));
    }
}
