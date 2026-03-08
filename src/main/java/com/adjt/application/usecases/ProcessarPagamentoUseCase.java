package com.adjt.application.usecases;

import com.adjt.application.ports.in.ProcessarPagamento;
import com.adjt.application.ports.out.EventoPagamentoAprovado;
import com.adjt.application.ports.out.EventoPagamentoPendente;
import com.adjt.application.ports.out.GatewayPagamentoExterno;
import com.adjt.application.ports.out.PagamentoPort;
import com.adjt.application.shared.EfetivarPagamentoService;
import com.adjt.domain.entities.Pagamento;

import java.math.BigDecimal;
import java.util.UUID;

public class ProcessarPagamentoUseCase implements ProcessarPagamento {

    private final EfetivarPagamentoService efetivarPagamentoService;

    public ProcessarPagamentoUseCase(PagamentoPort pagamentoPort, GatewayPagamentoExterno gatewayPagamentoExterno, EventoPagamentoPendente eventoPagamentoPendente, EventoPagamentoAprovado eventoPagamentoAprovado) {
        this.efetivarPagamentoService = new EfetivarPagamentoService(pagamentoPort, gatewayPagamentoExterno, eventoPagamentoPendente, eventoPagamentoAprovado);
    }

    @Override
    public void processar(BigDecimal valor, UUID usuarioId, UUID pedidoId) {
        Pagamento pagamento = new Pagamento(valor, usuarioId, pedidoId);
        efetivarPagamentoService.executarFluxoPagamento(pagamento);
    }
}
