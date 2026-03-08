package com.adjt.infrastructure.messaging.producer;

import com.adjt.application.ports.out.EventoPagamentoAprovado;
import com.adjt.domain.entities.Pagamento;
import com.adjt.infrastructure.messaging.dto.EventoPagamentoAprovadoMessage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class EventoPagamentoAprovadoProducerAdapter implements EventoPagamentoAprovado {
    private static final Logger LOG = LoggerFactory.getLogger(EventoPagamentoAprovadoProducerAdapter.class);

    @Inject
    @Channel("pagamento.aprovado.out")
    Emitter<EventoPagamentoAprovadoMessage> pagamentoAprovadoEmitter;

    @Override
    public void notificaPagamentoAprovado(Pagamento pagamento) {
        LOG.info("Notificando evento de pagamento aprovado: {}", pagamento);

        EventoPagamentoAprovadoMessage eventoPagamentoPendente = new EventoPagamentoAprovadoMessage(pagamento.getId(), pagamento.getPedidoId());
        pagamentoAprovadoEmitter.send(eventoPagamentoPendente);
    }
}
