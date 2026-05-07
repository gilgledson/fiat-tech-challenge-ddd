package br.com.fiap.oficina.api.modules.catalogo.produto.application;

import br.com.fiap.oficina.api.modules.catalogo.produto.application.repository.ProdutoRepository;
import br.com.fiap.oficina.api.modules.catalogo.produto.application.usecase.DeletarProdutoUseCaseImpl;
import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Deletar Produto - Testes Unitários")
class DeletarProdutoUseCaseTest {

    private ProdutoRepository repository;
    private DeletarProdutoUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(ProdutoRepository.class);
        useCase = new DeletarProdutoUseCaseImpl(repository);
    }

    @Test
    @DisplayName("Deve deletar um produto")
    void deveDeletarProduto() {
        UUID id = UUID.randomUUID();
        when(repository.buscarPorId(id)).thenReturn(Optional.of(Mockito.mock(Produto.class)));
        useCase.executar(id);
        verify(repository).delete(id);
    }
}









