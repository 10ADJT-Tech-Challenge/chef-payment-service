package com.adjt.application.ports.out;

import com.adjt.domain.entities.Pagamento;

import java.util.Optional;
import java.util.UUID;

public interface PagamentoPort {
    void salvar(Pagamento pagamento);

    Optional<Pagamento> buscaPagamentoPorIdEPedidoId(UUID pagamentoId, UUID pedidoId);
}
