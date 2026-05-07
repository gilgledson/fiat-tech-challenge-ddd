package br.com.fiap.oficina.api.modules.catalogo.produto.application.usecase;

import br.com.fiap.oficina.api.modules.catalogo.produto.application.repository.ProdutoRepository;
import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.Produto;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class InativarProdutoUseCaseImpl implements InativarProdutoUseCase{
    private final ProdutoRepository repository;

    @Override
    public void executar(UUID id) {
        Produto produto = repository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado."));
        produto.inativar();

        repository.editar(produto);

    }
}






