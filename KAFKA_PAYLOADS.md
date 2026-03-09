# Especificação dos Tópicos Kafka - Chef Payment Service

Este documento descreve os contratos de mensagens (payloads JSON) utilizados nos tópicos Kafka integrados ao microsserviço de pagamentos.

## 1. Tópico de Entrada: `pedido.criado`

**Descrição:** Evento recebido quando um novo pedido é criado no sistema de pedidos, solicitando o processamento do pagamento.

**DTO Java:** `EventoSolicitacaoPagamentoMessage`

**Exemplo de Payload JSON:**
```json
{
  "valor": 150.50,
  "usuarioId": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
  "pedidoId": "f1e2d3c4-b5a6-0987-6543-210987fedcba"
}
```

**Campos:**
*   `valor` (Number/Decimal): O valor total do pedido a ser processado.
*   `usuarioId` (String/UUID): Identificador único do cliente que realizou o pedido.
*   `pedidoId` (String/UUID): Identificador único do pedido gerado pelo serviço de pedidos.

---

## 2. Tópico de Saída: `pagamento.aprovado`

**Descrição:** Evento publicado quando o pagamento é processado e aprovado com sucesso pelo gateway externo.

**DTO Java:** `EventoPagamentoAprovadoMessage`

**Exemplo de Payload JSON:**
```json
{
  "pagamentoId": "98765432-1234-5678-90ab-cdef01234567",
  "pedidoId": "f1e2d3c4-b5a6-0987-6543-210987fedcba"
}
```

**Campos:**
*   `pagamentoId` (String/UUID): Identificador único do pagamento gerado internamente pelo serviço de pagamentos.
*   `pedidoId` (String/UUID): Identificador do pedido original ao qual este pagamento se refere.

---

## 3. Tópico de Saída: `pagamento.pendente`

**Descrição:** Evento publicado quando ocorre uma falha no processamento (recusa do gateway, erro técnico ou timeout), indicando que o pagamento não foi concluído ou está em estado de erro.

**DTO Java:** `EventoPagamentoPendenteMessage`

**Exemplo de Payload JSON:**
```json
{
  "pagamentoId": "98765432-1234-5678-90ab-cdef01234567",
  "pedidoId": "f1e2d3c4-b5a6-0987-6543-210987fedcba"
}
```

**Campos:**
*   `pagamentoId` (String/UUID): Identificador único do pagamento gerado internamente.
*   `pedidoId` (String/UUID): Identificador do pedido original.
