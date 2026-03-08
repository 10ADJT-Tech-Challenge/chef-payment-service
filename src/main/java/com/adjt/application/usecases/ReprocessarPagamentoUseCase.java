package com.adjt.application.usecases;

import com.adjt.application.exceptions.PagamentoNaoEncontradoException;
import com.adjt.application.ports.in.ReprocessarPagamento;
import com.adjt.application.ports.out.EventoPagamentoAprovado;
import com.adjt.application.ports.out.EventoPagamentoPendente;
import com.adjt.application.ports.out.GatewayPagamentoExterno;
import com.adjt.application.ports.out.PagamentoPort;
import com.adjt.application.shared.EfetivarPagamentoService;
import com.adjt.domain.entities.Pagamento;

import java.util.UUID;

public class ReprocessarPagamentoUseCase implements ReprocessarPagamento {

    private final EfetivarPagamentoService efetivarPagamentoService;
    private final PagamentoPort pagamentoPort;

    public ReprocessarPagamentoUseCase(PagamentoPort pagamentoPort, GatewayPagamentoExterno gatewayPagamentoExterno, EventoPagamentoPendente eventoPagamentoPendente, EventoPagamentoAprovado eventoPagamentoAprovado) {
        this.efetivarPagamentoService = new EfetivarPagamentoService(pagamentoPort, gatewayPagamentoExterno, eventoPagamentoPendente, eventoPagamentoAprovado);
        this.pagamentoPort = pagamentoPort;
    }

    @Override
    public void reprocessar(UUID pagamentoId, UUID pedidoId) {
        Pagamento pagamento = pagamentoPort.buscaPagamentoPorIdEPedidoId(pagamentoId, pedidoId)
                .orElseThrow(() -> new PagamentoNaoEncontradoException(pagamentoId));

        efetivarPagamentoService.executarFluxoPagamento(pagamento);
    }
}
