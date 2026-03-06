package com.adjt.infrastructure.config;

import com.adjt.application.ports.in.ProcessarPagamento;
import com.adjt.application.ports.out.GatewayPagamentoExterno;
import com.adjt.application.ports.out.PagamentoPort;
import com.adjt.application.usecases.ProcessarPagamentoUseCase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;

@ApplicationScoped
public class UseCaseConfig {

    @Produces
    @ApplicationScoped
    public ProcessarPagamento processarPagamentoUseCase(PagamentoPort pagamentoPort, GatewayPagamentoExterno gatewayPagamentoExterno) {
        return new ProcessarPagamentoUseCase(pagamentoPort, gatewayPagamentoExterno);
    }
}
