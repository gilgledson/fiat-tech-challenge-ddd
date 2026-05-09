package br.com.fiap.oficina.api.modules.catalogo.servico.api.controller;

import br.com.fiap.oficina.api.modules.identidade.domain.valueobject.PerfilUsuario;
import br.com.fiap.oficina.api.modules.catalogo.servico.api.dto.ServicoResponse;
import br.com.fiap.oficina.api.modules.catalogo.servico.api.dto.ServicosRequest;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.*;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.dto.ServicoOutput;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.ProdutoSugerido;
import br.com.fiap.oficina.api.shared.api.dto.PaginaResponse;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;

@Path("/api/servicos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Serviços", description = "Gestão de catalogo de serviços")
@RequiredArgsConstructor
public class ServicoController {
    private final CadastrarServicoUseCase cadastrarServicoUseCase;
    private final ListarServicosUseCase listarServicosUseCase;
    private final EditarServicoUseCase editarServicoUseCase;
    private final AtivarServicoUseCase ativarServicoUseCase;
    private final InativarServicoUseCase inativarServicoUseCase;
    private final DeletarServicoUseCase deletarServicoUseCase;

    @GET
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.MECANICO,
            PerfilUsuario.Constants.ATENDENTE })
    @Operation(summary = "Listar serviços")
    public Response listar(
            @QueryParam("pagina") @Parameter(description = "Número da página (começando do 0)", example = "0") @DefaultValue("0") @Min(value = 0, message = "Página não deve ser negativa") int pagina,

            @Parameter(description = "Quantidade máxima de itens retornados por página", example = "10") @QueryParam("tamanho") @DefaultValue("10") @Min(value = 1, message = "O tamanho mínimo da página é 1") @Max(value = 100, message = "O tamanho máximo permitido por página é 100 para evitar sobrecarga") int tamanho,

            @Parameter(description = "Se true, inclui na listagem os produtos que foram inativados logicamente") @QueryParam("incluir_inativos") @DefaultValue("false") boolean incluirInativos) {
        var paginaServicos = listarServicosUseCase.executar(pagina, tamanho, incluirInativos);
        var response = PaginaResponse.fromDomain(paginaServicos.map(ServicoResponse::fromOutput));
        return Response.ok().entity(response).build();
    }

    @POST
    @Path("/{id}/ativacao")
    @RolesAllowed(PerfilUsuario.Constants.ADMIN)
    @Operation(summary = "Ativar um serviço", description = "Ativa um serviço existente.")
    public Response ativar(
            @Parameter(description = "Id do produto", example = "e0281660-5826-4c1f-8cab-238cdb1ac328") @PathParam("id") UUID id) {
        ativarServicoUseCase.executar(id);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed(PerfilUsuario.Constants.ADMIN)
    @Operation(summary = "Inativar um serviço", description = "Inativa um serviço existente.")
    public Response inativar(
            @QueryParam("permanente") @Parameter(description = "Se true, deleta o serviço permanentemente", example = "false") @DefaultValue("false") boolean permanente,
            @Parameter(description = "Id do produto", example = "e0281660-5826-4c1f-8cab-238cdb1ac328") @PathParam("id") UUID id) {
        if (permanente) {
            deletarServicoUseCase.executar(id);
        } else {
            inativarServicoUseCase.executar(id);
        }
        return Response.ok().build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed(PerfilUsuario.Constants.ADMIN)
    @Operation(summary = "Editar um serviço", description = "Atualiza os dados de um serviço existente.")
    public Response editar(
            @Parameter(description = "Id do produto", example = "e0281660-5826-4c1f-8cab-238cdb1ac328") @PathParam("id") UUID id,
            @Valid ServicosRequest request) {

        List<ProdutoSugerido> produtoSugeridosEntity = request.mapearProdutosParaDominio();

        ServicoOutput servico = editarServicoUseCase.executar(id, request.nome(), request.tipo(), request.precoBase(),
                produtoSugeridosEntity);

        return Response.status(Response.Status.OK).entity(servico).build();
    }

    @POST
    @RolesAllowed(PerfilUsuario.Constants.ADMIN)
    @Operation(summary = "Cadastrar novo serviço")
    public Response salvar(@Valid ServicosRequest request) {
        List<ProdutoSugerido> produtoSugerido = request.mapearProdutosParaDominio();

        ServicoOutput servico = cadastrarServicoUseCase.execute(
                request.nome(),
                request.tipo(),
                request.precoBase(),
                produtoSugerido);

        ServicoResponse response = ServicoResponse.fromOutput(servico);

        return Response.status(Response.Status.CREATED).entity(response).build();
    }

}
