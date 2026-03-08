package com.adjt.application.shared;

import com.adjt.application.exceptions.FalhaPagamentoException;
import com.adjt.application.exceptions.PagamentoNaoAceitoException;
import com.adjt.application.ports.out.EventoPagamentoAprovado;
import com.adjt.application.ports.out.EventoPagamentoPendente;
import com.adjt.application.ports.out.GatewayPagamentoExterno;
import com.adjt.application.ports.out.PagamentoPort;
import com.adjt.domain.entities.Pagamento;
import com.adjt.domain.exceptions.PagamentoStatusInvalidoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EfetivarPagamentoService {
    private static final Logger LOG = LoggerFactory.getLogger(EfetivarPagamentoService.class);

    private final PagamentoPort pagamentoPort;
    private final GatewayPagamentoExterno gateway;
    private final EventoPagamentoPendente eventoPagamentoPendente;
    private final EventoPagamentoAprovado eventoPagamentoAprovado;

    public EfetivarPagamentoService(PagamentoPort pagamentoPort, GatewayPagamentoExterno gateway, EventoPagamentoPendente eventoPagamentoPendente, EventoPagamentoAprovado eventoPagamentoAprovado) {
        this.pagamentoPort = pagamentoPort;
        this.gateway = gateway;
        this.eventoPagamentoPendente = eventoPagamentoPendente;
        this.eventoPagamentoAprovado = eventoPagamentoAprovado;
    }

    public void executarFluxoPagamento(Pagamento pagamento) {
        try {
            pagamento.validaPodeSolicitarAprovacao();
            realizarPagamento(pagamento);
            eventoPagamentoAprovado.notificaPagamentoAprovado(pagamento);
        } catch (PagamentoStatusInvalidoException ignored) {
        } catch (FalhaPagamentoException e) {
            LOG.warn("Falha ao processar pagamento", e);
            eventoPagamentoPendente.notificaPagamentoPendente(pagamento);
        } catch (Exception e) {
            LOG.error("Erro inesperado ao processar pagamento", e);
            eventoPagamentoPendente.notificaPagamentoPendente(pagamento);
        } finally {
            pagamentoPort.salvar(pagamento);
        }
    }

    private void realizarPagamento(Pagamento pagamento) {
        if (!gateway.processarPagamento(pagamento))
            throw new PagamentoNaoAceitoException("Pagamento não aceito pelo gateway");

        pagamento.aprovar();
    }
}