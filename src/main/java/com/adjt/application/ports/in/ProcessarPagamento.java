package com.adjt.application.ports.in;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProcessarPagamento {
    void processar(BigDecimal valor, UUID userId, UUID pedidoId);
}
