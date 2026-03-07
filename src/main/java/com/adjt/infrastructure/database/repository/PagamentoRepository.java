package com.adjt.infrastructure.database.repository;

import com.adjt.application.ports.out.PagamentoPort;
import com.adjt.domain.Pagamento;
import com.adjt.infrastructure.database.entity.PagamentoEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PagamentoRepository implements PanacheRepository<PagamentoEntity>, PagamentoPort {
    @Override
    @Transactional
    public void salvar(Pagamento pagamento) {
        PagamentoEntity entity = new PagamentoEntity();
        entity.id = pagamento.getId();
        entity.usarioId = pagamento.getUsarioId();
        entity.pedidoID = pagamento.getPedidoId();
        entity.valor = pagamento.getValor();
        entity.status = pagamento.getStatus().name();

        persist(entity);
    }
}
