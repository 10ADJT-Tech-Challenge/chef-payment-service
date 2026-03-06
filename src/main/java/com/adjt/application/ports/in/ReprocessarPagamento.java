package com.adjt.application.ports.in;

import com.adjt.domain.Pagamento;

public interface ReprocessarPagamento {
    void reprocessar(Pagamento pagamento);
}
