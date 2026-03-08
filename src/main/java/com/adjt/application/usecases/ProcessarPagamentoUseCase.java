package com.adjt.application.usecases;

import com.adjt.application.exceptions.FalhaPagamentoException;
import com.adjt.application.exceptions.PagamentoNaoAceitoException;
import com.adjt.application.ports.in.ProcessarPagamento;
import com.adjt.application.ports.out.EventoPagamentoAprovado;
import com.adjt.application.ports.out.GatewayPagamentoExterno;
import com.adjt.application.ports.out.EventoPagamentoPendente;
import com.adjt.application.ports.out.PagamentoPort;
import com.adjt.domain.Pagamento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.UUID;

public class ProcessarPagamentoUseCase implements ProcessarPagamento {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessarPagamentoUseCase.class);

    private final PagamentoPort pagamentoPort;
    private final GatewayPagamentoExterno gateway;
    private final EventoPagamentoPendente eventoPagamentoPendente;
    private final EventoPagamentoAprovado eventoPagamentoAprovado;

    public ProcessarPagamentoUseCase(PagamentoPort filaPort, GatewayPagamentoExterno gatewayPagamentoExterno, EventoPagamentoPendente eventoPagamentoPendente, EventoPagamentoAprovado eventoPagamentoAprovado) {
        this.pagamentoPort = filaPort;
        this.gateway = gatewayPagamentoExterno;
        this.eventoPagamentoPendente = eventoPagamentoPendente;
        this.eventoPagamentoAprovado = eventoPagamentoAprovado;
    }

    @Override
    public void processar(BigDecimal valor, UUID usuarioId, UUID pedidoId) {
        Pagamento pagamento = new Pagamento(valor, usuarioId, pedidoId);

        try {
            realizarPagamento(pagamento); // se sucesso, aprova o pagamento e publica evento pagamento.aprovado
            eventoPagamentoAprovado.notificaPagamentoAprovado(pagamento);
        } catch (FalhaPagamentoException e) {
            LOG.warn("Falha ao processar pagamento", e);
            eventoPagamentoPendente.notificaPagamentoPendente(pagamento); // se erro, publica evento pagamento.pendente
        } catch (Exception exception){
            LOG.error("Erro inesperado ao processar pagamento", exception);
            eventoPagamentoPendente.notificaPagamentoPendente(pagamento); // Garante que falhas inesperadas também notifiquem pendência/erro
        } finally {
            pagamentoPort.salvar(pagamento);
        }
    }

    private void realizarPagamento(Pagamento pagamento) {
        if (!gateway.processarPagamento(pagamento)) // chama serviço externo para processar o pagamento
            throw new PagamentoNaoAceitoException("Pagamento não aceito pelo gateway");

        pagamento.aprovar();
    }
}
