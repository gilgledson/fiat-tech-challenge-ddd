package br.com.fiap.oficina.api.modules.catalogo.produto.application.usecase;

import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.Produto;
import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.UnidadeMedida;

import java.math.BigDecimal;

public interface CadastrarProdutoUseCase {
    public Produto executar(String nome, String codigoBarras, BigDecimal precoUnitario, BigDecimal quantidade,
            UnidadeMedida unidadeMedida);
}






