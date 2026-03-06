package com.adjt.application.ports.out;

import com.adjt.domain.Pagamento;

public interface GatewayPagamentoExterno {

    boolean processarPagamento(Pagamento pagamento);

}
