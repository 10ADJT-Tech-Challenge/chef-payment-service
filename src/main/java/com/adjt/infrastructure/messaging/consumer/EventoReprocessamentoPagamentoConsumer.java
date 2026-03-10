package com.adjt.infrastructure.messaging.consumer;

import com.adjt.application.ports.in.ReprocessarPagamento;
import com.adjt.infrastructure.messaging.dto.EventoPagamentoPendenteMessage;
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
public class EventoReprocessamentoPagamentoConsumer {
    private final Logger LOG = LoggerFactory.getLogger(EventoReprocessamentoPagamentoConsumer.class);

    @Inject
    ReprocessarPagamento reprocessarPagamento;

    @Blocking("pedido-pool")
    @Transactional
    @Incoming("pagamento-pendente-in")
    public CompletionStage<Void> consumirMensagem(Message<EventoPagamentoPendenteMessage> mensagem) {
        EventoPagamentoPendenteMessage payload = mensagem.getPayload();
        LOG.info("Evento de reprocessamento de pagamento recebido: {}", payload);

        reprocessarPagamento.reprocessar(payload.pagamentoId(), payload.pedidoId());

        return mensagem.ack();
    }
}
