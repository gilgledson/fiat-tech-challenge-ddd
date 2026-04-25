package br.com.fiap.oficina.api.modules.catalogo.produto.api.controller;

import br.com.fiap.oficina.api.modules.catalogo.produto.api.dto.ProdutoRequest;
import br.com.fiap.oficina.api.modules.catalogo.produto.api.dto.ProdutoResponse;
import br.com.fiap.oficina.api.modules.catalogo.produto.application.usecase.*;
import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.Produto;
import br.com.fiap.oficina.api.shared.api.dto.PaginaResponse;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.UUID;

@Path("/api/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Produtos", description = "Endpoints para gestão de produtos")
public class ProdutoController {
    private final CadastrarProdutoUseCase cadastrarProdutoUseCase;
    private final ListarProdutosUseCase listarProdutosUseCase;
    private final EditarProdutoUseCase editarProdutoUseCase;
    private final InativarProdutoUseCase inativarProdutoUseCase;
    private final AtivarProdutoUseCase ativarProdutoUseCase;
    private final DeletarProdutoUseCase deletarProdutoUseCase;

    @Inject
    public ProdutoController(
            CadastrarProdutoUseCase cadastrarProdutoUseCase,
            ListarProdutosUseCase listarProdutosUseCase,
            EditarProdutoUseCase editarProdutoUseCase,
            InativarProdutoUseCase inativarProdutoUseCase,
            AtivarProdutoUseCase ativarProdutoUseCase,
            DeletarProdutoUseCase deletarProdutoUseCase) {
        this.cadastrarProdutoUseCase = cadastrarProdutoUseCase;
        this.listarProdutosUseCase = listarProdutosUseCase;
        this.editarProdutoUseCase = editarProdutoUseCase;
        this.inativarProdutoUseCase = inativarProdutoUseCase;
        this.ativarProdutoUseCase = ativarProdutoUseCase;
        this.deletarProdutoUseCase = deletarProdutoUseCase;
    }

    @POST
    @Operation(summary = "Cadastrar um novo produto")
    public Response cadastrarProduto(@Valid ProdutoRequest request) {
        Produto novoProduto = cadastrarProdutoUseCase.executar(
                request.nome(),
                request.codigoBarras(),
                request.precoUnitario(),
                request.quantidadeEstoque(),
                request.unidadeMedida());

        ProdutoResponse responseDto = ProdutoResponse.fromEntity(novoProduto);

        return Response.status(Response.Status.CREATED).entity(responseDto).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Editar um produto", description = "Atualiza os dados de um produto existente.")
    public Response editarProduto(
            @Parameter(description = "ID do Produto", example = "e0281660-5826-4c1f-8cab-238cdb1ac328") @PathParam("id") UUID id,
            @Valid ProdutoRequest request) {

        Produto produto = editarProdutoUseCase.executar(
                id,
                request.nome(),
                request.codigoBarras(),
                request.precoUnitario(),
                request.quantidadeEstoque(),
                request.unidadeMedida());
        ProdutoResponse responseDto = ProdutoResponse.fromEntity(produto);

        return Response.status(Response.Status.OK).entity(responseDto).build();
    }

    @DELETE
    @Path("/{id}")
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
    @Transactional
    @Operation(summary = "Reativar um produto", description = "Remove o carimbo de data da exclusão lógica, tornando o produto visível novamente.")
    @APIResponse(responseCode = "200", description = "Produto reativado com sucesso")
    public Response ativarProduto(
            @Parameter(description = "ID do Produto", example = "e0281660-5826-4c1f-8cab-238cdb1ac328") @PathParam("id") UUID id) {
        ativarProdutoUseCase.executar(id);
        return Response.ok().build();
    }

    @GET
    public Response listarProdutos(
            @Parameter(description = "Número da página (começa em 0)", example = "0") @QueryParam("pagina") @DefaultValue("0") @Min(value = 0, message = "A página não pode ser negativa") int pagina,

            @Parameter(description = "Quantidade máxima de itens retornados por página", example = "10") @QueryParam("tamanho") @DefaultValue("10") @Min(value = 1, message = "O tamanho mínimo da página é 1") @Max(value = 100, message = "O tamanho máximo permitido por página é 100 para evitar sobrecarga") int tamanho,

            @Parameter(description = "Se true, inclui na listagem os produtos que foram inativados logicamente") @QueryParam("incluir_inativos") @DefaultValue("false") boolean incluirInativos) {
        var paginaProdutos = listarProdutosUseCase.executar(pagina, tamanho, incluirInativos);
        var response = PaginaResponse.fromDomain(paginaProdutos.map(ProdutoResponse::fromEntity));
        return Response.status(Response.Status.OK).entity(response).build();
    }

}
