package com.adjt.infrastructure.gateways;

import com.adjt.infrastructure.gateways.dto.SolicitacaoPagamentoRequest;
import com.adjt.infrastructure.gateways.dto.PagamentoSolicitadoResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "api-pagamento-externo")
@Path("/requisicao")
public interface PagamentoExternoRestClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    PagamentoSolicitadoResponse solicitarPagamento(SolicitacaoPagamentoRequest request);
}
