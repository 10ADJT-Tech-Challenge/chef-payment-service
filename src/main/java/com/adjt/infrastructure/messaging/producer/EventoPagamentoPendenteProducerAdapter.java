package com.adjt.infrastructure.messaging.producer;

import com.adjt.application.ports.out.EventoPagamentoPendente;
import com.adjt.domain.entities.Pagamento;
import com.adjt.infrastructure.messaging.dto.EventoPagamentoPendenteMessage;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class EventoPagamentoPendenteProducerAdapter implements EventoPagamentoPendente {
    private static final Logger LOG = LoggerFactory.getLogger(EventoPagamentoPendenteProducerAdapter.class);

    @Inject
    @Channel("pagamento.pendente.out")
    Emitter<EventoPagamentoPendenteMessage> pagamentoPendenteEmitter;

    @Override
    public void notificaPagamentoPendente(Pagamento pagamento) {
        LOG.info("Notificando evento de pagamento pendente: {}", pagamento);

        EventoPagamentoPendenteMessage eventoPagamentoPendente = new EventoPagamentoPendenteMessage(pagamento.getId(), pagamento.getPedidoId());
        pagamentoPendenteEmitter.send(eventoPagamentoPendente);
    }
}
