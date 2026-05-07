package br.com.fiap.oficina.api.modules.catalogo.produto.application.usecase;

import br.com.fiap.oficina.api.modules.catalogo.produto.application.repository.ProdutoRepository;
import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.Produto;
import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.UnidadeMedida;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class CadastrarProdutoUseCaseImpl implements CadastrarProdutoUseCase {
    private final ProdutoRepository repository;

    @Override
    public Produto executar(String nome, String codigoBarras, BigDecimal precoUnitario, BigDecimal quantidade,
            UnidadeMedida unidadeMedida) {

        if (repository.buscarPorCodigoBarras(codigoBarras).isPresent()) {
            throw new IllegalArgumentException("Já existe um produto com este código de barras.");
        }

        Produto produto = new Produto(nome, codigoBarras, precoUnitario, quantidade, BigDecimal.ZERO, unidadeMedida);
        repository.salvar(produto);
        return produto;
    }
}






