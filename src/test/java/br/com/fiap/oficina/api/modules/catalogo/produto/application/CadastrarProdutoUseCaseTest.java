package br.com.fiap.oficina.api.modules.catalogo.produto.application;

import br.com.fiap.oficina.api.modules.catalogo.produto.application.repository.ProdutoRepository;
import br.com.fiap.oficina.api.modules.catalogo.produto.application.usecase.CadastrarProdutoUseCaseImpl;
import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.Produto;
import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.UnidadeMedida;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("CadastrarProdutoUseCase - Testes Unitários")
class CadastrarProdutoUseCaseTest {

    @Mock
    private ProdutoRepository repository;

    private CadastrarProdutoUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new CadastrarProdutoUseCaseImpl(repository);
    }

    @Test
    @DisplayName("Deve cadastrar produto com dados válidos e chamar salvar")
    void deveCadastrarComSucesso() {
        when(repository.buscarPorCodigoBarras("7891234567890")).thenReturn(Optional.empty());

        Produto resultado = useCase.executar("Óleo 5W40", "7891234567890",
                new BigDecimal("45.90"), new BigDecimal("10"), UnidadeMedida.UN);

        assertNotNull(resultado);
        assertEquals("Óleo 5W40", resultado.getNome());
        verify(repository, times(1)).salvar(any(Produto.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando código de barras já existir")
    void deveLancarExcecaoParaCodigoBarrasDuplicado() {
        Produto existente = new Produto("Produto Existente", "7891234567890",
                new BigDecimal("10"), BigDecimal.ONE, BigDecimal.ZERO, UnidadeMedida.UN);
        when(repository.buscarPorCodigoBarras("7891234567890")).thenReturn(Optional.of(existente));

        assertThrows(IllegalArgumentException.class, () ->
            useCase.executar("Produto Novo", "7891234567890",
                    new BigDecimal("20"), BigDecimal.ONE, UnidadeMedida.UN)
        );

        verify(repository, never()).salvar(any());
    }
}









