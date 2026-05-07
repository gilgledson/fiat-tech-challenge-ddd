package br.com.fiap.oficina.api.modules.catalogo.produto.application.usecase;

import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.Produto;
import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.UnidadeMedida;

import java.math.BigDecimal;
import java.util.UUID;

public interface EditarProdutoUseCase {
    public Produto executar(UUID id, String nome, String codigoBarras, BigDecimal precoUnitario, BigDecimal quantidade,
            UnidadeMedida unidadeMedida);

}






