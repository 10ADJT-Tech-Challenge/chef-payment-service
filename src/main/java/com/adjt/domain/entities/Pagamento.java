package com.adjt.domain.entities;

import com.adjt.domain.exceptions.PagamentoStatusInvalidoException;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

import static com.adjt.domain.entities.StatusPagamento.APROVADO;
import static com.adjt.domain.entities.StatusPagamento.PENDENTE;

@Getter
public class Pagamento {

    private final UUID id;
    private final UUID usuarioId;
    private final UUID pedidoId;
    private final BigDecimal valor;
    private StatusPagamento status;

    public Pagamento(BigDecimal valor, UUID usuarioId, UUID pedidoId) {
        this(UUID.randomUUID(), valor, usuarioId, pedidoId, PENDENTE);
    }

    public Pagamento(UUID pagamentoId, BigDecimal valor, UUID usuarioId, UUID pedidoId, StatusPagamento status) {
        this.id = pagamentoId;
        this.usuarioId = usuarioId;
        this.pedidoId = pedidoId;
        this.valor = valor;
        this.status = status;
    }

    public void validaPodeSolicitarAprovacao() {
        if (isAprovado())
            throw new PagamentoStatusInvalidoException("Pagamento já se encontra aprovado");
    }

    public void aprovar() {
        if (!isPendente())
            throw new PagamentoStatusInvalidoException("Pagamento deveria estar pendente para ser aprovado");

        this.status = APROVADO;
    }

    private boolean isPendente() {
        return status == PENDENTE;
    }

    private boolean isAprovado() {
        return status == APROVADO;
    }

    public int getValorEmCentavos() {
        return valor.multiply(BigDecimal.valueOf(100)).intValue();
    }

}
