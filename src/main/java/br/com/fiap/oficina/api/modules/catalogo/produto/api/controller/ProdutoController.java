package br.com.fiap.oficina.api.modules.catalogo.produto.api.controller;

import br.com.fiap.oficina.api.modules.catalogo.produto.api.dto.ProdutoRequest;
import br.com.fiap.oficina.api.modules.catalogo.produto.api.dto.ProdutoResponse;
import br.com.fiap.oficina.api.modules.catalogo.produto.application.usecase.*;
import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.Produto;
import br.com.fiap.oficina.api.modules.identidade.domain.valueobject.PerfilUsuario;
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

@Path("/api/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Produtos", description = "Endpoints para gestão de produtos")
@RequiredArgsConstructor
public class ProdutoController {
    private final CadastrarProdutoUseCase cadastrarProdutoUseCase;
    private final ListarProdutosUseCase listarProdutosUseCase;
    private final EditarProdutoUseCase editarProdutoUseCase;
    private final InativarProdutoUseCase inativarProdutoUseCase;
    private final AtivarProdutoUseCase ativarProdutoUseCase;
    private final DeletarProdutoUseCase deletarProdutoUseCase;

    @POST
    @RolesAllowed(PerfilUsuario.Constants.ADMIN)
    @Operation(summary = "Cadastrar um novo produto")
    public Response cadastrarProduto(@Valid ProdutoRequest request) {
        Produto novoProduto = cadastrarProdutoUseCase.executar(
                request.nome(),
                request.codigoBarras(),
                request.precoUnitario(),
                request.quantidadeEstoqueFisico(),
                request.unidadeMedida());

        ProdutoResponse responseDto = ProdutoResponse.fromEntity(novoProduto);

        return Response.status(Response.Status.CREATED).entity(responseDto).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed(PerfilUsuario.Constants.ADMIN)
    @Operation(summary = "Editar um produto", description = "Atualiza os dados de um produto existente.")
    public Response editarProduto(
            @Parameter(description = "ID do Produto", example = "e0281660-5826-4c1f-8cab-238cdb1ac328") @PathParam("id") UUID id,
            @Valid ProdutoRequest request) {

        Produto produto = editarProdutoUseCase.executar(
                id,
                request.nome(),
                request.codigoBarras(),
                request.precoUnitario(),
                request.quantidadeEstoqueFisico(),
                request.unidadeMedida());
        ProdutoResponse responseDto = ProdutoResponse.fromEntity(produto);

        return Response.status(Response.Status.OK).entity(responseDto).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed(PerfilUsuario.Constants.ADMIN)
    @Operation(summary = "Inativar / Deletar um produto", description = "Oculta o produto das listagens. Use ?permanente=true para exclusão permanente.")
    public Response inativarProduto(
            @Parameter(description = "ID do Produto", example = "e0281660-5826-4c1f-8cab-238cdb1ac328") @PathParam("id") UUID id,

            @Parameter(description = "Se true, apaga o registro fisicamente do banco de dados") @QueryParam("permanente") @DefaultValue("false") boolean permanente) {
        if (permanente) {
            deletarProdutoUseCase.executar(id);
        } else {
            inativarProdutoUseCase.executar(id);
        }
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("/{id}/ativacao")
    @RolesAllowed(PerfilUsuario.Constants.ADMIN)
    @Transactional
    @Operation(summary = "Reativar um produto", description = "Remove o carimbo de data da exclusão lógica, tornando o produto visível novamente.")
    @APIResponse(responseCode = "200", description = "Produto reativado com sucesso")
    public Response ativarProduto(
            @Parameter(description = "ID do Produto", example = "e0281660-5826-4c1f-8cab-238cdb1ac328") @PathParam("id") UUID id) {
        ativarProdutoUseCase.executar(id);
        return Response.ok().build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.MECANICO,
            PerfilUsuario.Constants.ATENDENTE })
    @Operation(summary = "Buscar produto por ID")
    public Response buscarPorId(@PathParam("id") UUID id) {
        var pagina = listarProdutosUseCase.executar(0, 1000, true);
        var produto = pagina.itens().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));
        return Response.ok(ProdutoResponse.fromEntity(produto)).build();
    }

    @GET
    @RolesAllowed({ PerfilUsuario.Constants.ADMIN, PerfilUsuario.Constants.MECANICO,
            PerfilUsuario.Constants.ATENDENTE })
    public Response listarProdutos(
            @Parameter(description = "Número da página (começa em 0)", example = "0") @QueryParam("pagina") @DefaultValue("0") @Min(value = 0, message = "A página não pode ser negativa") int pagina,

            @Parameter(description = "Quantidade máxima de itens retornados por página", example = "10") @QueryParam("tamanho") @DefaultValue("10") @Min(value = 1, message = "O tamanho mínimo da página é 1") @Max(value = 100, message = "O tamanho máximo permitido por página é 100 para evitar sobrecarga") int tamanho,

            @Parameter(description = "Se true, inclui na listagem os produtos que foram inativados logicamente") @QueryParam("incluir_inativos") @DefaultValue("false") boolean incluirInativos) {
        var paginaProdutos = listarProdutosUseCase.executar(pagina, tamanho, incluirInativos);
        var response = PaginaResponse.fromDomain(paginaProdutos.map(ProdutoResponse::fromEntity));
        return Response.status(Response.Status.OK).entity(response).build();
    }

}
