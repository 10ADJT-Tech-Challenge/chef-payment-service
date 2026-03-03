# ==========================================
# Estágio 1: Build (Compilação)
# ==========================================
# Estágio 1: Build da Aplicação (usando o JDK)
# Usamos uma imagem JDK completa para compilar o código
FROM eclipse-temurin:21-jdk-jammy AS builder

# Define o diretório de trabalho
WORKDIR /app

# Copia os arquivos do Maven
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Dá permissão de execução ao wrapper e baixa as dependências (otimiza o cache do Docker)
RUN chmod +x ./mvnw && ./mvnw dependency:go-offline

# Copia o código fonte e compila o projeto
COPY src src
RUN ./mvnw package -DskipTests

# ==========================================
# Estágio 2: Runtime (Execução)
# ==========================================
FROM registry.access.redhat.com/ubi9/openjdk-21-runtime:1.24

ENV LANGUAGE='en_US:en'

# Copia os artefatos compilados do estágio anterior (builder)
COPY --chown=185 --from=builder /app/target/quarkus-app/lib/ /deployments/lib/
COPY --chown=185 --from=builder /app/target/quarkus-app/*.jar /deployments/
COPY --chown=185 --from=builder /app/target/quarkus-app/app/ /deployments/app/
COPY --chown=185 --from=builder /app/target/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE 8080
USER 185
ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

ENTRYPOINT [ "/opt/jboss/container/java/run/run-java.sh" ]