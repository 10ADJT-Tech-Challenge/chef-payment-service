package com.adjt.infrastructure.gateways;

import com.adjt.application.ports.out.GatewayPagamentoExterno;
import com.adjt.domain.Pagamento;
import com.adjt.infrastructure.gateways.dto.PagamentoSolicitadoResponse;
import com.adjt.infrastructure.gateways.dto.SolicitacaoPagamentoRequest;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class GatewayPagamentoExternoAdapter implements GatewayPagamentoExterno {

    @RestClient
    PagamentoExternoRestClient restClient;

    @Override
    public boolean processarPagamento(Pagamento pagamento) {
        SolicitacaoPagamentoRequest request = new SolicitacaoPagamentoRequest(
                pagamento.getValor(),
                pagamento.getId().toString(),
                pagamento.getUsarioId().toString()
        );

        try {
            PagamentoSolicitadoResponse response = restClient.solicitarPagamento(request);
            return "accepted".equalsIgnoreCase(response.status());
        } catch (Exception e) {
            System.err.println("Erro ao processar pagamento: " + e.getMessage());
            return false;
        }
    }
}
