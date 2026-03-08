package com.adjt.application.ports.out;

import com.adjt.domain.entities.Pagamento;

public interface EventoPagamentoPendente {
    void notificaPagamentoPendente(Pagamento pagamento);
}
