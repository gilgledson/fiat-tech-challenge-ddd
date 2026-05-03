package br.com.fiap.oficina.api.modules.faturamento.api.controller;

import br.com.fiap.oficina.api.modules.faturamento.application.usecase.consultas.BaixarFaturaPorOrdemServicoUseCase;
import br.com.fiap.oficina.api.modules.faturamento.application.usecase.consultas.BaixarFaturaUseCase;
import br.com.fiap.oficina.api.modules.faturamento.api.dto.ConfirmarPagamentoRequest;
import br.com.fiap.oficina.api.modules.faturamento.application.usecase.comandos.ConfirmarPagamentoFaturaUseCase;
import br.com.fiap.oficina.api.modules.faturamento.domain.entity.Fatura;
import br.com.fiap.oficina.api.modules.identidade.domain.valueobject.PerfilUsuario;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import java.util.UUID;

@Path("/api/faturas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Faturamento", description = "Gestão de faturas e pagamentos")
public class FaturaController {

    @Inject
    ConfirmarPagamentoFaturaUseCase confirmarPagamentoFaturaUseCase;

    @Inject
    BaixarFaturaUseCase baixarFaturaUseCase;

    @Inject
    BaixarFaturaPorOrdemServicoUseCase baixarFaturaPorOrdemServicoUseCase;

    @POST
    @Path("/{id}/confirmar-pagamento")
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.ATENDENTE })
    @Operation(summary = "Confirmar pagamento de uma fatura")
    public Response confirmarPagamento(@PathParam("id") UUID id, @Valid ConfirmarPagamentoRequest request) {
        confirmarPagamentoFaturaUseCase.execute(id, request.metodoPagamento());
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.ATENDENTE })
    @Operation(summary = "Obter fatura por ID")
    @Produces("application/pdf")
    public Response buscarFaturaPorId(@PathParam("id") UUID id) {
        byte[] fatura = baixarFaturaUseCase.executar(id);
        return Response.ok(fatura).build();
    }

    @GET
    @Path("/ordem-servico/{id}")
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.ATENDENTE })
    @Operation(summary = "Obter fatura por ID da Ordem de Serviço")
    @Produces("application/pdf")
    public Response buscarFaturaPorOrdemServicoId(@PathParam("id") UUID id) {
        byte[] fatura = baixarFaturaPorOrdemServicoUseCase.executar(id);
        return Response.ok(fatura).build();
    }

    @GET
    @Path("/ordem-servico/{id}/json")
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.ATENDENTE })
    @Operation(summary = "Obter fatura por ID da Ordem de Serviço (JSON)")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarFaturaPorOrdemServicoIdJson(@PathParam("id") UUID id) {
        Fatura fatura = baixarFaturaPorOrdemServicoUseCase.buscarPorOrdemServicoId(id);
        return Response.ok(fatura).build();
    }
}
