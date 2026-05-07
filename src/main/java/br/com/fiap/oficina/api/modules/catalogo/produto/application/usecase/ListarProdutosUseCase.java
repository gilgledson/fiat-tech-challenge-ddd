package br.com.fiap.oficina.api.modules.catalogo.produto.application.usecase;

import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.Produto;
import br.com.fiap.oficina.api.shared.domain.entity.Pagina;

public interface ListarProdutosUseCase {
    public Pagina<Produto> executar(int pagina, int tamanhoLista, boolean incluirInativos);
}






