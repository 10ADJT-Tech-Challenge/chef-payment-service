package com.adjt.infrastructure.messaging;

import com.adjt.application.ports.in.ProcessarPagamento;
import com.adjt.infrastructure.messaging.dto.SolicitacaoPagamentoMessage;
import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class PagamentoConsumerAdapter {

    @ConfigProperty(name = "quarkus.rest-client.api-pagamento-externo.url", defaultValue = "not-configured")
    String restClientUrl;

    @ConfigProperty(name = "PAGAMENTO_EXTERNO_URL", defaultValue = "env-not-set")
    String envUrl;

    @Inject
    ProcessarPagamento processarPagamento;

    @Blocking
    @Transactional
    @Incoming("pedido.criado")
    public CompletionStage<Void> consumirMensagem(Message<SolicitacaoPagamentoMessage> mensagem) {
        System.out.println("=== DEBUG CONFIGURAÇÕES ===");
        System.out.println("URL do REST Client: " + restClientUrl);
        System.out.println("Variável de ambiente PAGAMENTO_EXTERNO_URL: " + envUrl);


        try {
            SolicitacaoPagamentoMessage payload = mensagem.getPayload();
            System.out.println("Recebendo solicitação de pagamento: " + payload);
            processarPagamento.processar(payload.valor(), payload.usuarioId(), payload.pedidoId());
            return mensagem.ack();
        } catch (Exception e) {
            System.err.println("Erro ao processar pagamento: " + e.getMessage());
            mensagem.nack(e);
            throw e;
        }
    }
}
