package com.adjt.application.ports.out;

import com.adjt.domain.entities.Pagamento;

public interface GatewayPagamentoExterno {
    boolean processarPagamento(Pagamento pagamento);
}
