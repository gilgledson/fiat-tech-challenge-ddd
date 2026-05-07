package br.com.fiap.oficina.api.modules.catalogo.produto.application;

import br.com.fiap.oficina.api.modules.catalogo.produto.application.repository.ProdutoRepository;
import br.com.fiap.oficina.api.modules.catalogo.produto.application.usecase.EditarProdutoUseCaseImpl;
import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.Produto;
import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.UnidadeMedida;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("EditarProdutoUseCase - Testes Unitários")
class EditarProdutoUseCaseTest {

    @Mock
    private ProdutoRepository repository;

    private EditarProdutoUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new EditarProdutoUseCaseImpl(repository);
    }

    @Test
    @DisplayName("Deve editar produto existente com sucesso")
    void deveEditarComSucesso() {
        UUID id = UUID.randomUUID();
        Produto produto = Produto.reconstituir(id, "Óleo Antigo", "123",
                new BigDecimal("40.00"), BigDecimal.TEN, BigDecimal.ZERO, UnidadeMedida.UN, Optional.empty());
        when(repository.buscarPorId(id)).thenReturn(Optional.of(produto));

        Produto resultado = useCase.executar(id, "Óleo Novo", "456",
                new BigDecimal("55.00"), BigDecimal.TEN, UnidadeMedida.LT);

        assertEquals("Óleo Novo", resultado.getNome());
        verify(repository, times(1)).editar(any(Produto.class));
    }

    @Test
    @DisplayName("Deve lançar NotFoundException quando produto não existir")
    void deveLancarNotFoundQuandoProdutoNaoExistir() {
        UUID id = UUID.randomUUID();
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
            useCase.executar(id, "Qualquer", "123",
                    new BigDecimal("10"), BigDecimal.ONE, UnidadeMedida.UN)
        );
    }
}









