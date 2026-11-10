package br.com.fiap.oficina.api.modules.integracao.api.controller;

import java.util.Optional;
import java.util.UUID;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.com.fiap.oficina.api.modules.identidade.domain.valueobject.PerfilUsuario;
import br.com.fiap.oficina.api.modules.integracao.application.ConsultarDadosOrcamentoUseCase;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

/**
 * API consumida pelo Billing Service (única comunicação síncrona entre os
 * microsserviços). O Billing repassa o JWT de quem pediu o orçamento, então
 * as mesmas regras de perfil das rotas de orçamento valem aqui.
 */
@Path("/api/integracao")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Integração", description = "Dados da OS para os outros microsserviços")
@RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.ATENDENTE })
@RequiredArgsConstructor
public class IntegracaoController {

    private final ConsultarDadosOrcamentoUseCase consultarDadosOrcamento;

    @GET
    @Path("/ordens/{id}/orcamento")
    @Operation(summary = "Itens aprovados e valor total de uma OS, para o orçamento")
    public Response buscarOrdemDeServico(@PathParam("id") UUID id) {
        return responder(consultarDadosOrcamento.buscarOrdemDeServico(id));
    }

    @GET
    @Path("/clientes/{id}")
    @Operation(summary = "Dados do cliente para o orçamento")
    public Response buscarCliente(@PathParam("id") UUID id) {
        return responder(consultarDadosOrcamento.buscarCliente(id));
    }

    @GET
    @Path("/veiculos/{id}")
    @Operation(summary = "Dados do veículo para o orçamento")
    public Response buscarVeiculo(@PathParam("id") UUID id) {
        return responder(consultarDadosOrcamento.buscarVeiculo(id));
    }

    private static Response responder(Optional<?> resultado) {
        return resultado
                .map(dados -> Response.ok(dados).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}
