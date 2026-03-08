package com.adjt.infrastructure.gateways;

import com.adjt.application.exceptions.GatewayPagamentoIndisponivelException;
import com.adjt.application.ports.out.GatewayPagamentoExterno;
import com.adjt.domain.Pagamento;
import com.adjt.infrastructure.gateways.dto.PagamentoSolicitadoResponse;
import com.adjt.infrastructure.gateways.dto.SolicitacaoPagamentoRequest;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.temporal.ChronoUnit;

@ApplicationScoped
public class GatewayPagamentoExternoAdapter implements GatewayPagamentoExterno {
    private static final Logger LOG = LoggerFactory.getLogger(GatewayPagamentoExternoAdapter.class);

    @RestClient
    PagamentoExternoRestClient restClient;

    @Override
    @Timeout(value = 1, unit = ChronoUnit.SECONDS)
    @Retry(maxRetries = 2)
    @Fallback(fallbackMethod = "fallbackFalhaProcessamento")
    @CircuitBreaker(requestVolumeThreshold = 4, // Número de requisições a serem examinadas antes de calcular a taxa de falha.
            failureRatio = 0.5,                 // A proporção de falhas (ex: 0.5 = 50%) necessária para abrir o circuito.
            delay = 2500,                       // Quanto tempo (em milissegundos) o circuito ficará aberto antes de tentar passar para o estado "Meio-Aberto".
            successThreshold = 1                // Quantas chamadas bem-sucedidas são necessárias no estado "Meio-Aberto" para fechar o circuito novamente.
    )
    public boolean processarPagamento(Pagamento pagamento) {
        LOG.info("Solicitação pagamento gateway externo: {}", pagamento);

        SolicitacaoPagamentoRequest request = new SolicitacaoPagamentoRequest(pagamento.getValorEmCentavos(), pagamento.getId().toString(), pagamento.getUsarioId().toString());

        PagamentoSolicitadoResponse response = restClient.solicitarPagamento(request);
        return response.isAceito();
    }

    public boolean fallbackFalhaProcessamento(Pagamento pagamento, Throwable erro) {
        LOG.error("Falha ao processar pagamento para o pedido {}. \nAcionando fallback. \nErro: {}", pagamento.getPedidoId(), erro.getMessage());

        throw new GatewayPagamentoIndisponivelException("Serviço de pagamento fora do ar após retentativas. Motivo: %s".formatted(erro.getMessage()));
    }
}
