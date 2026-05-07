package br.com.fiap.oficina.api.modules.catalogo.produto.application.usecase;

import br.com.fiap.oficina.api.modules.catalogo.produto.application.repository.ProdutoRepository;
import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.Produto;
import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.UnidadeMedida;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@RequiredArgsConstructor
public class EditarProdutoUseCaseImpl implements EditarProdutoUseCase {
    private final ProdutoRepository repository;

    @Override
    public Produto executar(UUID id, String nome, String codigoBarras, BigDecimal precoUnitario, BigDecimal quantidade,
            UnidadeMedida unidadeMedida) {
        Produto produto = repository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado."));

        if (codigoBarras != null && !codigoBarras.isBlank() && !codigoBarras.equals(produto.getCodigoBarras())) {
            repository.buscarPorCodigoBarras(codigoBarras).ifPresent(produtoExistente -> {
                throw new IllegalArgumentException("Já existe outro produto cadastrado com este código de barras.");
            });
        }
        produto.atualizarDados(nome, codigoBarras, precoUnitario, quantidade, produto.getQuantidadeEstoqueReservado(),
                unidadeMedida);
        repository.editar(produto);
        return produto;
    }
}






