package com.adjt.application.usecases;

import com.adjt.application.ports.in.ProcessarPagamento;
import com.adjt.application.ports.out.PagamentoPort;
import com.adjt.domain.Pagamento;

import java.math.BigDecimal;
import java.util.UUID;

public class ProcessarPagamentoUseCase implements ProcessarPagamento {
    private final PagamentoPort pagamentoPort;

    public ProcessarPagamentoUseCase(PagamentoPort filaPort) {
        this.pagamentoPort = filaPort;
    }

    @Override
    public void processar(BigDecimal valor, UUID userId, UUID pedidoId) {
        Pagamento pagamento = new Pagamento(valor, userId, pedidoId);

        // chama serviço externo para processar o pagamento
        // se sucesso, aprova o pagamento e publica evento
        // se erro, publica fila de erro
        pagamentoPort.salvar(pagamento);
    }
}
