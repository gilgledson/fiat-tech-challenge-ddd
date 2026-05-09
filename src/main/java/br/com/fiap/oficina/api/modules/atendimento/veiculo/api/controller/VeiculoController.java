package br.com.fiap.oficina.api.modules.atendimento.veiculo.api.controller;

import br.com.fiap.oficina.api.modules.identidade.domain.valueobject.PerfilUsuario;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.api.dto.VeiculoRequest;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.api.dto.VeiculoResponse;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.dto.VeiculoOutput;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.usecase.*;
import br.com.fiap.oficina.api.shared.api.dto.PaginaResponse;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.UUID;

@Path("/api/veiculos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Veículos", description = "Endpoints para gestão de veículos")
@RequiredArgsConstructor
public class VeiculoController {

    private final CadastrarVeiculoUseCase cadastrarVeiculoUseCase;
    private final ListarVeiculosUseCase listarVeiculosUseCase;
    private final EditarVeiculoUseCase editarVeiculoUseCase;
    private final InativarVeiculoUseCase inativarVeiculoUseCase;
    private final AtivarVeiculoUseCase ativarVeiculoUseCase;
    private final DeletarVeiculoUseCase deletarVeiculoUseCase;

    @POST
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.ATENDENTE })
    @Operation(summary = "Cadastrar um novo veículo")
    public Response cadastrarVeiculo(@Valid VeiculoRequest request) {
        VeiculoOutput output = cadastrarVeiculoUseCase.executar(
                request.clienteId(),
                request.placa(),
                request.marca(),
                request.modelo(),
                request.ano());

        VeiculoResponse responseDto = VeiculoResponse.fromOutput(output);

        return Response.status(Response.Status.CREATED).entity(responseDto).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.ATENDENTE })
    @Operation(summary = "Editar um veículo", description = "Atualiza os dados de um veículo existente.")
    public Response editarVeiculo(
            @Parameter(description = "ID do Veículo", example = "e0281660-5826-4c1f-8cab-238cdb1ac328") @PathParam("id") UUID id,
            @Valid VeiculoRequest request) {
        VeiculoOutput output = editarVeiculoUseCase.executar(
                id,
                request.clienteId(),
                request.placa(),
                request.marca(),
                request.modelo(),
                request.ano());
        VeiculoResponse responseDto = VeiculoResponse.fromOutput(output);

        return Response.status(Response.Status.OK).entity(responseDto).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.ATENDENTE })
    @Operation(summary = "Inativar / Deletar um veículo", description = "Oculta o veículo das listagens. Use ?permanente=true para exclusão permanente.")
    public Response inativarVeiculo(
            @Parameter(description = "ID do Veículo", example = "e0281660-5826-4c1f-8cab-238cdb1ac328") @PathParam("id") UUID id,

            @Parameter(description = "Se true, apaga o registro fisicamente do banco de dados") @QueryParam("permanente") @DefaultValue("false") boolean permanente) {
        if (permanente) {
            deletarVeiculoUseCase.executar(id);
        } else {
            inativarVeiculoUseCase.executar(id);
        }
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("/{id}/ativacao")
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.ATENDENTE })
    @Transactional
    @Operation(summary = "Reativar um veículo", description = "Remove o carimbo de data da exclusão lógica, tornando o veículo visível novamente.")
    @APIResponse(responseCode = "200", description = "Veículo reativado com sucesso")
    public Response ativarVeiculo(
            @Parameter(description = "ID do Veículo", example = "e0281660-5826-4c1f-8cab-238cdb1ac328") @PathParam("id") UUID id) {
        ativarVeiculoUseCase.executar(id);
        return Response.ok().build();
    }

    @GET
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.MECANICO,
            PerfilUsuario.Constants.ATENDENTE })
    @Operation(summary = "Listar veículos", description = "Retorna uma lista paginada de veículos.")
    public Response listarVeiculos(
            @Parameter(description = "Número da página (começa em 0)", example = "0") @QueryParam("pagina") @DefaultValue("0") @Min(value = 0, message = "A página não pode ser negativa") int pagina,

            @Parameter(description = "Quantidade máxima de itens retornados por página", example = "10") @QueryParam("tamanho") @DefaultValue("10") @Min(value = 1, message = "O tamanho mínimo da página é 1") @Max(value = 100, message = "O tamanho máximo permitido por página é 100 para evitar sobrecarga") int tamanho,

            @Parameter(description = "Se true, inclui na listagem os veículos que foram inativados logicamente") @QueryParam("incluir_inativos") @DefaultValue("false") boolean incluirInativos) {
        var paginaVeiculos = listarVeiculosUseCase.executar(pagina, tamanho, incluirInativos);
        var response = PaginaResponse.fromDomain(paginaVeiculos.map(VeiculoResponse::fromOutput));
        return Response.status(Response.Status.OK).entity(response).build();
    }
}
