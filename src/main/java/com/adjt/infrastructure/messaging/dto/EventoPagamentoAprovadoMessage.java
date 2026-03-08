package com.adjt.infrastructure.messaging.dto;

import java.util.UUID;

public record EventoPagamentoAprovadoMessage(UUID pagamentoId, UUID pedidoId) {
}
