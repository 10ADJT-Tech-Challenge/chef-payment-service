package com.adjt.application.ports.out;

import com.adjt.domain.Pagamento;

public interface EventoPagamentoAprovado {
    void notificaPagamentoAprovado(Pagamento pagamento);
}
