package br.com.fiap.oficina.api.modules.catalogo.produto.application.usecase;

import br.com.fiap.oficina.api.modules.catalogo.produto.application.repository.ProdutoRepository;
import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.Produto;
import br.com.fiap.oficina.api.shared.domain.entity.Pagina;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListarProdutosUseCaseImpl implements ListarProdutosUseCase {
    private final ProdutoRepository repository;

    @Override
    public Pagina<Produto> executar(int pagina, int tamanhoLista, boolean incluirInativos) {
        return repository.listarTodos(pagina, tamanhoLista, incluirInativos);
    }
}






