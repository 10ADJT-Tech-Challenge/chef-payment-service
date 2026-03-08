package com.adjt.infrastructure.messaging.consumer;

import com.adjt.application.ports.in.ProcessarPagamento;
import com.adjt.infrastructure.messaging.dto.EventoSolicitacaoPagamentoMessage;
import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class EventoSolicitacaoPagamentoConsumerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(EventoSolicitacaoPagamentoConsumerAdapter.class);

    @Inject
    ProcessarPagamento processarPagamento;

    @Blocking
    @Transactional
    @Incoming("pedido.criado")
    public CompletionStage<Void> consumirMensagem(Message<EventoSolicitacaoPagamentoMessage> mensagem) {
        EventoSolicitacaoPagamentoMessage payload = mensagem.getPayload();

        LOG.info("Evento pagamento recebido: {}", payload);
        processarPagamento.processar(payload.valor(), payload.usuarioId(), payload.pedidoId());

        return mensagem.ack();
    }
}
