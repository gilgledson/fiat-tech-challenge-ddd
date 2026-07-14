package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.api.controller;

import br.com.fiap.oficina.api.modules.identidade.domain.valueobject.PerfilUsuario;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.api.dto.*;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.consulta.BuscarOrdemDeServicoUseCase;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.consulta.ListarOrdensDeServicoUseCase;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.ciclovida.*;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.estado.*;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.item.*;
import br.com.fiap.oficina.api.shared.api.dto.PaginaResponse;
import br.com.fiap.oficina.api.shared.infrastructure.security.WebhookAuthValidator;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.UUID;

@Path("/api/ordens")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Ordem de Servico", description = "Gestao de ordens de servico da oficina")
@RequiredArgsConstructor
public class OrdemDeServicoController {

    private final JsonWebToken jwt;

    private final AbrirOrdemDeServicoUseCase abrirOrdemDeServicoUseCase;
    private final BuscarOrdemDeServicoUseCase buscarOrdemDeServicoUseCase;
    private final ListarOrdensDeServicoUseCase listarOrdensDeServicoUseCase;
    private final IniciarDiagnosticoUseCase iniciarDiagnosticoUseCase;
    private final ConcluirDiagnosticoUseCase concluirDiagnosticoUseCase;
    private final RejeitarOrcamentoUseCase rejeitarOrcamentoUseCase;
    private final AdicionarServicoOrdemDeServicoUseCase adicionarServicoUseCase;
    private final RemoverProdutoOrdemDeServicoUseCase removerProdutoUseCase;
    private final RemoverServicoOrdemDeServicoUseCase removerServicoUseCase;
    private final IniciarExecucaoServicoUseCase iniciarExecucaoServicoUseCase;
    private final FinalizarExecucaoServicoUseCase finalizarExecucaoServicoUseCase;

    // UseCases da Máquina de Estados (Task-Based)
    private final AprovarOrdemDeServicoUseCase aprovarOrdemDeServicoUseCase;
    private final IniciarExecucaoOsUseCase iniciarExecucaoOsUseCase;
    private final ConcluirExecucaoOrdemDeServicoUseCase concluirExecucaoOrdemDeServicoUseCase;
    private final CancelarOrdemDeServicoUseCase cancelarOrdemDeServicoUseCase;
    private final EntregarVeiculoUseCase entregarVeiculoUseCase;
    private final WebhookAuthValidator webhookAuthValidator;

    @POST
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.ATENDENTE })
    @Operation(summary = "Abrir nova Ordem de Serviço, opcionalmente já com serviços e peças identificados")
    public Response abrir(@Valid AbrirOrdemDeServicoRequest request) {
        var output = abrirOrdemDeServicoUseCase.executar(
                request.clienteId(),
                request.veiculoId(),
                request.descricaoProblema(),
                request.servicos());
        return Response.status(Response.Status.CREATED).entity(OrdemDeServicoResponse.fromOutput(output)).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.MECANICO, PerfilUsuario.Constants.ATENDENTE,
            PerfilUsuario.Constants.CLIENTE })
    @Operation(summary = "Buscar Ordem de Serviço por ID")
    public Response buscarPorId(@PathParam("id") UUID id) {
        var output = buscarOrdemDeServicoUseCase.executar(id);
        return Response.ok(OrdemDeServicoResponse.fromOutput(output)).build();
    }

    @GET
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.MECANICO, PerfilUsuario.Constants.ATENDENTE,
            PerfilUsuario.Constants.CLIENTE })
    @Operation(summary = "Listar todas as Ordens de Serviço")
    public Response listar(
            @QueryParam("clienteId") UUID clienteId,
            @QueryParam("veiculoId") UUID veiculoId,
            @QueryParam("pagina") @DefaultValue("0") @Min(0) int pagina,
            @QueryParam("tamanho") @DefaultValue("10") @Min(1) @Max(100) int tamanho,
            @QueryParam("incluir_inativas") @DefaultValue("false") boolean incluirInativas) {
        var paginaOrdens = listarOrdensDeServicoUseCase.executar(clienteId, veiculoId, pagina, tamanho,
                incluirInativas);
        var response = PaginaResponse.fromDomain(paginaOrdens.map(OrdemDeServicoResponse::fromOutput));
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{id}/produtos/{produtoId}")
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.MECANICO })
    @Operation(summary = "Remover produto da Ordem de Serviço")
    public Response removerProduto(
            @PathParam("id") UUID id,
            @PathParam("produtoId") UUID produtoId) {
        var output = removerProdutoUseCase.executar(id, produtoId);
        return Response.ok(OrdemDeServicoResponse.fromOutput(output)).build();
    }

    @POST
    @Path("/{id}/servicos")
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.MECANICO })
    @Operation(summary = "Adicionar servico a Ordem de Servico")
    public Response adicionarServico(
            @PathParam("id") UUID id,
            @Valid AdicionarServicoRequest request) {
        var output = adicionarServicoUseCase.executar(id, request);
        return Response.ok(OrdemDeServicoResponse.fromOutput(output)).build();
    }

    @DELETE
    @Path("/{id}/servicos/{servicoId}")
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.MECANICO })
    @Operation(summary = "Remover serviço da Ordem de Serviço")
    public Response removerServico(
            @PathParam("id") UUID id,
            @PathParam("servicoId") UUID servicoId) {
        var output = removerServicoUseCase.executar(id, servicoId);
        return Response.ok(OrdemDeServicoResponse.fromOutput(output)).build();
    }

    @POST
    @Path("/{id}/iniciar-diagnostico")
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.MECANICO })
    @Operation(summary = "Iniciar diagnóstico do veículo")
    public Response iniciarDiagnostico(@PathParam("id") UUID id) {
        var output = iniciarDiagnosticoUseCase.executar(id);
        return Response.ok(OrdemDeServicoResponse.fromOutput(output)).build();
    }

    @POST
    @Path("/{id}/concluir-diagnostico")
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.MECANICO })
    @Operation(summary = "Concluir diagnóstico e enviar para aprovação do cliente")
    public Response concluirDiagnostico(@PathParam("id") UUID id) {
        var output = concluirDiagnosticoUseCase.executar(id);
        return Response.ok(OrdemDeServicoResponse.fromOutput(output)).build();
    }

    // =======================================================
    // MAQUINA DE ESTADOS (TASK-BASED REST) E CANCELAMENTO
    // =======================================================

    @POST
    @Path("/{id}/rejeitar")
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.ATENDENTE, PerfilUsuario.Constants.CLIENTE })
    @Operation(summary = "Rejeitar Orçamento (Cliente reprovou)")
    public Response rejeitar(@PathParam("id") UUID id) {
        var output = rejeitarOrcamentoUseCase.executar(id);
        return Response.ok(OrdemDeServicoResponse.fromOutput(output)).build();
    }

    @POST
    @Path("/{id}/aprovar")
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.ATENDENTE, PerfilUsuario.Constants.CLIENTE })
    @Operation(summary = "Aprovar Orçamento (Cliente aprovou serviços)")
    public Response aprovar(
            @PathParam("id") UUID id,
            @Valid AprovarServicoRequest request) {
        aprovarOrdemDeServicoUseCase.executar(id, request);
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}/aprovacao-externa")
    @PermitAll
    @Operation(summary = "Webhook para notificação externa de aprovação ou recusa do orçamento (ex: portal do cliente, gateway de pagamento). "
            + "Autenticado por secret compartilhado no header X-Webhook-Secret, não por JWT de usuário.")
    public Response aprovacaoExterna(
            @PathParam("id") UUID id,
            @HeaderParam("X-Webhook-Secret") String webhookSecret,
            @Valid AprovarServicoRequest request) {
        if (!webhookAuthValidator.valido(webhookSecret)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        aprovarOrdemDeServicoUseCase.executar(id, request);
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}/iniciar-execucao")
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.MECANICO })
    @Operation(summary = "Mudar status para Em Execução (Carro no elevador)")
    public Response iniciarExecucao(@PathParam("id") UUID id) {
        iniciarExecucaoOsUseCase.executar(id);
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}/concluir-execucao")
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.MECANICO })
    @Operation(summary = "Concluir Execucao da Ordem de Servico (Pronto para orcamento)")
    public Response concluirExecucao(@PathParam("id") UUID id) {
        concluirExecucaoOrdemDeServicoUseCase.executar(id);
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}/servicos/{servicoId}/iniciar-execucao")
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.MECANICO })
    @Operation(summary = "Iniciar execução de um serviço específico")
    public Response iniciarExecucaoServico(
            @PathParam("id") UUID id,
            @PathParam("servicoId") UUID servicoId) {
        String usuarioIdStr = jwt.getClaim("usuario_id");
        UUID usuarioId = usuarioIdStr != null ? UUID.fromString(usuarioIdStr) : null;
        iniciarExecucaoServicoUseCase.executar(id, servicoId, usuarioId);
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}/servicos/{servicoId}/finalizar-execucao")
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.MECANICO })
    @Operation(summary = "Finalizar execução de um serviço específico")
    public Response finalizarExecucaoServico(
            @PathParam("id") UUID id,
            @PathParam("servicoId") UUID servicoId) {
        finalizarExecucaoServicoUseCase.executar(id, servicoId);
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}/cancelar")
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.ATENDENTE })
    @Operation(summary = "Cancelar Ordem de Serviço (Soft Delete de Negócio)")
    public Response cancelar(
            @PathParam("id") UUID id,
            @Valid CancelarOrdemRequest request) {
        cancelarOrdemDeServicoUseCase.executar(id, request.motivo());
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}/entregar")
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.ATENDENTE })
    @Operation(summary = "Realizar a entrega do veículo (Finaliza o ciclo de vida)")
    public Response entregar(@PathParam("id") UUID id) {
        entregarVeiculoUseCase.executar(id);
        return Response.noContent().build();
    }
}
