package com.adjt.infrastructure.messaging;

import com.adjt.application.ports.in.ProcessarPagamento;
import com.adjt.infrastructure.messaging.dto.SolicitacaoPagamentoMessage;
import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class PagamentoConsumerAdapter {

    @Inject
    ProcessarPagamento processarPagamento;

    @Blocking
    @Transactional
    @Incoming("pedido.criado")
    public CompletionStage<Void> consumirMensagem(Message<SolicitacaoPagamentoMessage> mensagem) {
        SolicitacaoPagamentoMessage payload = mensagem.getPayload();
        System.out.println("Recebendo solicitação de pagamento: " + payload);
        processarPagamento.processar(payload.valor(), payload.usuarioId(), payload.pedidoId());
        return mensagem.ack();
    }
}
