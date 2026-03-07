package com.adjt.infrastructure.gateways.dto;

public record SolicitacaoPagamentoRequest(int valor,
                                          String pagamento_id,
                                          String cliente_id) {
}
