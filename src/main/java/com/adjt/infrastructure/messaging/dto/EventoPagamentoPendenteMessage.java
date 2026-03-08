package com.adjt.infrastructure.messaging.dto;

import java.util.UUID;

public record EventoPagamentoPendenteMessage(UUID pagamentoId, UUID pedidoId) {
}
