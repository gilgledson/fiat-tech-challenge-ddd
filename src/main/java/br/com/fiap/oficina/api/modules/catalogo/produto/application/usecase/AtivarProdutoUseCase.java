package br.com.fiap.oficina.api.modules.catalogo.produto.application.usecase;

import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.Produto;

import java.util.UUID;

public interface AtivarProdutoUseCase {
    public Produto executar(UUID id);
}






