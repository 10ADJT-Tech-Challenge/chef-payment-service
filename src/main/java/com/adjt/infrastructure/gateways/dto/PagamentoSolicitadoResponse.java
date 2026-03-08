package com.adjt.infrastructure.gateways.dto;

public record PagamentoSolicitadoResponse(String status) {

    public static final String PAGAMENTO_ACEITO = "accepted";

    public boolean isAceito() {
        return PAGAMENTO_ACEITO.equalsIgnoreCase(status);
    }
}
