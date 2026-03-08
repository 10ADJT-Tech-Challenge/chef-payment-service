package com.adjt.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class Pagamento {

    private final UUID id;
    private final UUID usarioId;
    private final UUID pedidoId;
    private final BigDecimal valor;
    private StatusPagamento status;

    public Pagamento(BigDecimal valor, UUID usarioId, UUID pedidoId) {
        this(UUID.randomUUID(), valor, usarioId, pedidoId, StatusPagamento.PENDENTE);
    }

    public Pagamento(UUID pagamentoId, BigDecimal valor, UUID usarioId, UUID pedidoId, StatusPagamento status) {
        this.id = pagamentoId;
        this.usarioId = usarioId;
        this.pedidoId = pedidoId;
        this.valor = valor;
        this.status = status;
    }

    public void aprovar() {
        this.status = StatusPagamento.APROVADO;
    }

    public int getValorEmCentavos() {
        return valor.multiply(BigDecimal.valueOf(100)).intValue();
    }

}
