package com.adjt.infrastructure.gateways.dto;

import java.math.BigDecimal;

public record SolicitacaoPagamentoRequest(BigDecimal valor,
                                          String pagador_id,
                                          String cliente_id) {
}
