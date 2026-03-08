# Chef Payment Service

Microsserviço responsável pelo processamento de pagamentos do ecossistema **Chef**, desenvolvido com **Java 21** e **Quarkus**.

Este serviço segue os princípios da **Arquitetura Hexagonal (Ports and Adapters)** para garantir desacoplamento entre as regras de negócio e as tecnologias de infraestrutura.

## 🚀 Tecnologias Utilizadas

*   **Java 21**
*   **Quarkus Framework** (Supersonic Subatomic Java)
*   **PostgreSQL** (Persistência de dados via Hibernate ORM com Panache)
*   **Apache Kafka** (Mensageria assíncrona via SmallRye Reactive Messaging)
*   **MicroProfile Fault Tolerance** (Resiliência: Circuit Breaker, Retry, Fallback, Timeout)
*   **Docker** & **Docker Compose**

## architecture Arquitetura do Projeto

O projeto está estruturado em camadas conforme a Arquitetura Hexagonal:

```
src/main/java/com/adjt
├── application
│   ├── ports         # Interfaces de entrada (In) e saída (Out)
│   ├── usecases      # Implementação das regras de negócio
│   └── exceptions    # Exceções de domínio
├── domain            # Entidades e objetos de valor do núcleo (Core)
└── infrastructure    # Adaptadores externos
    ├── config        # Configurações do Quarkus/CDI
    ├── database      # Repositórios e Entidades JPA (PostgreSQL)
    ├── gateways      # Clientes REST para serviços externos
    └── messaging     # Consumidores e Produtores Kafka
```

## ⚙️ Configuração

O serviço utiliza variáveis de ambiente para configuração. Abaixo estão as principais variáveis definidas no `application.yaml`:

| Variável | Descrição | Valor Padrão |
| :--- | :--- | :--- |
| `DB_HOST` | Host do banco de dados PostgreSQL | `localhost` |
| `DB_PORT` | Porta do banco de dados | `5432` |
| `DB_NAME` | Nome do banco de dados | `pagamentosdb` |
| `DB_USER` | Usuário do banco | `postgres` |
| `DB_PASSWORD` | Senha do banco | `postgres` |
| `KAFKA_HOST` | Host do Broker Kafka | `localhost` |
| `KAFKA_PORT` | Porta do Broker Kafka | `9092` |
| `PAGAMENTO_EXTERNO_URL` | URL do Gateway de Pagamento Externo | `http://localhost:8089` |

## 📡 Contratos de Mensageria (Kafka)

O serviço interage com outros microsserviços através de tópicos Kafka.

*   **Consome:** `pedido.criado`
*   **Produz:** `pagamento.aprovado`, `pagamento.pendente`

Para detalhes completos sobre os payloads JSON, consulte o arquivo [KAFKA_PAYLOADS.md](./KAFKA_PAYLOADS.md).

## 🛠️ Como Executar

### Pré-requisitos
*   JDK 21+
*   Docker (para subir as dependências de infraestrutura)

### 1. Subir Infraestrutura (Banco de Dados e Kafka)
Certifique-se de ter um PostgreSQL e um Kafka rodando acessíveis nas portas configuradas, ou utilize um `docker-compose.yml` (se disponível no workspace principal).

### 2. Modo de Desenvolvimento (Dev Mode)
Para rodar a aplicação em modo de desenvolvimento com *live coding*:

```bash
./mvnw quarkus:dev
```

### 3. Empacotar e Rodar (JVM)
Para gerar o JAR e executar:

```bash
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar
```

### 4. Executar via Docker
Para construir e rodar a imagem Docker JVM:

```bash
# Build da imagem
docker build -f src/main/docker/Dockerfile.jvm -t quarkus/chef-payment-service-jvm .

# Executar container
docker run -i --rm -p 8080:8080 --network host quarkus/chef-payment-service-jvm
```

## 🛡️ Resiliência

O serviço implementa padrões de resiliência na comunicação com o Gateway de Pagamento Externo:
*   **Timeout:** 1 segundo.
*   **Retry:** 2 tentativas automáticas em caso de falha.
*   **Circuit Breaker:** Abre o circuito se 50% das requisições falharem (janela de 4 requisições).
*   **Fallback:** Em caso de falha total, o pagamento é marcado como pendente e um evento é disparado para tratamento posterior.
