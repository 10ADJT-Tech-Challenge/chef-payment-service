package com.adjt.infrastructure.config;

import com.adjt.application.ports.in.ProcessarPagamento;
import com.adjt.application.ports.in.ReprocessarPagamento;
import com.adjt.application.ports.out.EventoPagamentoAprovado;
import com.adjt.application.ports.out.EventoPagamentoPendente;
import com.adjt.application.ports.out.GatewayPagamentoExterno;
import com.adjt.application.ports.out.PagamentoPort;
import com.adjt.application.usecases.ProcessarPagamentoUseCase;
import com.adjt.application.usecases.ReprocessarPagamentoUseCase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;

@ApplicationScoped
public class UseCaseConfig {

    @Produces
    @ApplicationScoped
    public ProcessarPagamento processarPagamentoUseCase(PagamentoPort pagamentoPort, GatewayPagamentoExterno gatewayPagamentoExterno, EventoPagamentoPendente eventoPagamentoPendente, EventoPagamentoAprovado eventoPagamentoAprovado) {
        return new ProcessarPagamentoUseCase(pagamentoPort, gatewayPagamentoExterno, eventoPagamentoPendente, eventoPagamentoAprovado);
    }

    @Produces
    @ApplicationScoped
    public ReprocessarPagamento reprocessarPagamentoUseCase(PagamentoPort pagamentoPort, GatewayPagamentoExterno gatewayPagamentoExterno, EventoPagamentoPendente eventoPagamentoPendente, EventoPagamentoAprovado eventoPagamentoAprovado) {
        return new ReprocessarPagamentoUseCase(pagamentoPort, gatewayPagamentoExterno, eventoPagamentoPendente, eventoPagamentoAprovado);
    }
}
