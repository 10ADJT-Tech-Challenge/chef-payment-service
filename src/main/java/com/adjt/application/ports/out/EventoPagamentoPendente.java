package com.adjt.application.ports.out;

import com.adjt.domain.Pagamento;

public interface EventoPagamentoPendente {
    void notificaPagamentoPendente(Pagamento pagamento);
}
