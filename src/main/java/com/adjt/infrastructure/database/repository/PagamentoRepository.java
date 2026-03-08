package com.adjt.infrastructure.database.repository;

import com.adjt.application.ports.out.PagamentoPort;
import com.adjt.domain.entities.Pagamento;
import com.adjt.domain.entities.StatusPagamento;
import com.adjt.infrastructure.database.entity.PagamentoEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class PagamentoRepository implements PanacheRepositoryBase<PagamentoEntity, UUID>, PagamentoPort {
    @Override
    @Transactional
    public void salvar(Pagamento pagamento) {
        Optional<PagamentoEntity> optionalEntity = findByIdOptional(pagamento.getId());
        if (optionalEntity.isEmpty())
            persist(toEntity(pagamento));
        else
            atualizaStatus(pagamento, optionalEntity.get());
    }

    @Override
    public Optional<Pagamento> buscaPagamentoPorIdEPedidoId(UUID pagamentoId, UUID pedidoId) {
        Optional<PagamentoEntity> entity = find("id = ?1 and pedidoId = ?2", pagamentoId, pedidoId).firstResultOptional();

        return entity.map(pagamentoEntity -> new Pagamento(pagamentoEntity.id, pagamentoEntity.valor, pagamentoEntity.usuarioId, pagamentoEntity.pedidoId, StatusPagamento.valueOf(pagamentoEntity.status)));
    }

    private static PagamentoEntity toEntity(Pagamento pagamento) {
        PagamentoEntity entity = new PagamentoEntity();
        entity.id = pagamento.getId();
        entity.usuarioId = pagamento.getUsuarioId();
        entity.pedidoId = pagamento.getPedidoId();
        entity.valor = pagamento.getValor();
        entity.status = pagamento.getStatus().name();
        return entity;
    }

    private static void atualizaStatus(Pagamento pagamento, PagamentoEntity optionalEntity) {
        optionalEntity.status = pagamento.getStatus().name();
    }
}
