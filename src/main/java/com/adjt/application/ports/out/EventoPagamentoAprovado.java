package com.adjt.application.ports.out;

import com.adjt.domain.entities.Pagamento;

public interface EventoPagamentoAprovado {
    void notificaPagamentoAprovado(Pagamento pagamento);
}
