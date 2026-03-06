package com.adjt.infrastructure.database.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
public class PagamentoEntity extends PanacheEntityBase {

    @Id
    public UUID id;
    public UUID usarioId;
    public UUID pedidoID;
    public BigDecimal valor;
    public String status;
}
