package com.adjt.application.ports.in;

import java.util.UUID;

public interface ReprocessarPagamento {

    void reprocessar(UUID pagamentoId, UUID pedidoId);
}
