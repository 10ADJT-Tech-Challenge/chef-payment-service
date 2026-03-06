package com.adjt.application.usecases;

import com.adjt.application.ports.in.ProcessarPagamento;
import com.adjt.application.ports.out.GatewayPagamentoExterno;
import com.adjt.application.ports.out.PagamentoPort;
import com.adjt.domain.Pagamento;

import java.math.BigDecimal;
import java.util.UUID;

public class ProcessarPagamentoUseCase implements ProcessarPagamento {
    private final PagamentoPort pagamentoPort;
    private final GatewayPagamentoExterno gateway;

    public ProcessarPagamentoUseCase(PagamentoPort filaPort, GatewayPagamentoExterno gatewayPagamentoExterno) {
        this.pagamentoPort = filaPort;
        this.gateway = gatewayPagamentoExterno;
    }

    @Override
    public void processar(BigDecimal valor, UUID userId, UUID pedidoId) {
        Pagamento pagamento = new Pagamento(valor, userId, pedidoId);

        // chama serviço externo para processar o pagamento
        if (gateway.processarPagamento(pagamento)) {
            pagamento.pagar();
        } else {
            // aqui poderia ser um status de falha ou algo do tipo
            System.err.println("Pagamento falhou para pedido: " + pedidoId);
        }

        // se sucesso, aprova o pagamento e publica evento pagamento.aprovado
        // se erro, publica evento pagamento.pendente
        pagamentoPort.salvar(pagamento);
    }
}
