package com.adjt.infrastructure.messaging.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record SolicitacaoPagamentoMessage(BigDecimal valor,
                                          UUID usuarioId,
                                          UUID pedidoId) {
}
