package br.com.fiap.oficina.api.modules.atendimento.cliente.api.controller;

import br.com.fiap.oficina.api.modules.identidade.domain.valueobject.PerfilUsuario;
import br.com.fiap.oficina.api.modules.atendimento.cliente.api.dto.ClienteRequest;
import br.com.fiap.oficina.api.modules.atendimento.cliente.api.dto.ClienteResponse;
import br.com.fiap.oficina.api.modules.atendimento.cliente.application.dto.ClienteOutput;
import br.com.fiap.oficina.api.modules.atendimento.cliente.application.usecase.*;
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

@Path("/api/clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Clientes", description = "Endpoints para gestão de clientes")
@RequiredArgsConstructor
public class ClienteController {
    private final CadastrarClienteUseCase cadastrarClienteUseCase;
    private final ListarClientesUseCase listarClientesUseCase;
    private final EditarClienteUseCase editarClienteUseCase;
    private final InativarClienteUseCase inativarClienteUseCase;
    private final AtivarClienteUseCase ativarClienteUseCase;
    private final DeletarClienteUseCase deletarClienteUseCase;

    @POST
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.ATENDENTE })
    @Operation(summary = "Cadastrar um novo cliente")
    public Response cadastrarCliente(@Valid ClienteRequest request) {

        ClienteOutput output = cadastrarClienteUseCase.executar(
                request.usuarioId(),
                request.nome(),
                request.email(),
                request.cpfCnpj(),
                request.telefone(),
                request.endereco().toDomain());

        ClienteResponse responseDto = ClienteResponse.fromOutput(output);

        return Response.status(Response.Status.CREATED).entity(responseDto).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.ATENDENTE })
    @Operation(summary = "Editar um cliente", description = "Atualiza os dados de um cliente existente.")
    public Response editarCliente(
            @Parameter(description = "ID do Cliente", example = "e0281660-5826-4c1f-8cab-238cdb1ac328") @PathParam("id") UUID id,
            @Valid ClienteRequest request) {
        ClienteOutput output = editarClienteUseCase.executar(
                id,
                request.nome(),
                request.email(),
                request.telefone(),
                request.endereco().toDomain());
        ClienteResponse responseDto = ClienteResponse.fromOutput(output);

        return Response.status(Response.Status.OK).entity(responseDto).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.ATENDENTE })
    @Operation(summary = "Inativar / Deletar um cliente", description = "Oculta o cliente das listagens. Use ?permanente=true para exclusão permanente.")
    public Response inativarCliente(
            @Parameter(description = "ID do Cliente", example = "e0281660-5826-4c1f-8cab-238cdb1ac328") @PathParam("id") UUID id,

            @Parameter(description = "Se true, apaga o registro fisicamente do banco de dados") @QueryParam("permanente") @DefaultValue("false") boolean permanente) {
        if (permanente) {
            deletarClienteUseCase.executar(id);
        } else {
            inativarClienteUseCase.executar(id);
        }
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("/{id}/ativacao")
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.ATENDENTE })
    @Transactional
    @Operation(summary = "Reativar um cliente", description = "Remove o carimbo de data da exclusão lógica, tornando o cliente visível novamente.")
    @APIResponse(responseCode = "200", description = "Cliente reativado com sucesso")
    public Response ativarCliente(
            @Parameter(description = "ID do Cliente", example = "e0281660-5826-4c1f-8cab-238cdb1ac328") @PathParam("id") UUID id) {
        ativarClienteUseCase.executar(id);
        return Response.ok().build();
    }

    @GET
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.MECANICO,
            PerfilUsuario.Constants.ATENDENTE })
    @Operation(summary = "Listar clientes", description = "Retorna uma lista paginada de clientes.")
    public Response listarClientes(
            @Parameter(description = "Número da página (começa em 0)", example = "0") @QueryParam("pagina") @DefaultValue("0") @Min(value = 0, message = "A página não pode ser negativa") int pagina,

            @Parameter(description = "Quantidade máxima de itens retornados por página", example = "10") @QueryParam("tamanho") @DefaultValue("10") @Min(value = 1, message = "O tamanho mínimo da página é 1") @Max(value = 100, message = "O tamanho máximo permitido por página é 100 para evitar sobrecarga") int tamanho,

            @Parameter(description = "Se true, inclui na listagem os clientes que foram inativados logicamente") @QueryParam("incluir_inativos") @DefaultValue("false") boolean incluirInativos) {
        var paginaClientes = listarClientesUseCase.executar(pagina, tamanho, incluirInativos);
        var response = PaginaResponse.fromDomain(paginaClientes.map(ClienteResponse::fromOutput));
        return Response.status(Response.Status.OK).entity(response).build();
    }
}
