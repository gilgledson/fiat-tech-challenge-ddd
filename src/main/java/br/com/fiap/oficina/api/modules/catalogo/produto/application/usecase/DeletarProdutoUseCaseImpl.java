package br.com.fiap.oficina.api.modules.catalogo.produto.application.usecase;

import br.com.fiap.oficina.api.modules.catalogo.produto.application.repository.ProdutoRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class DeletarProdutoUseCaseImpl implements DeletarProdutoUseCase {
    private final ProdutoRepository repository;

    @Override
    public void executar(UUID id) {
        repository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Produto " + id + "não encontrado"));
        repository.delete(id);
    }
}






