package br.com.fiap.oficina.api.modules.catalogo.servico.application;

import br.com.fiap.oficina.api.modules.catalogo.produto.application.repository.ProdutoRepository;
import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.Produto;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.validator.ProdutoSugeridoValidator;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.ProdutoSugerido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@DisplayName("Validador de Produto Sugerido - Testes Unitários")
class ProdutoSugeridoValidatorTest {

    private ProdutoRepository produtoRepository;
    private ProdutoSugeridoValidator validator;

    @BeforeEach
    void setUp() {
        produtoRepository = Mockito.mock(ProdutoRepository.class);
        validator = new ProdutoSugeridoValidator(produtoRepository);
    }

    @Test
    @DisplayName("Deve validar lista vazia ou nula com sucesso")
    void deveValidarListaVazia() {
        validator.validar(null);
        validator.validar(List.of());
    }

    @Test
    @DisplayName("Deve validar produtos ativos com sucesso")
    void deveValidarProdutosAtivos() {
        UUID id = UUID.randomUUID();
        Produto mockProduto = Mockito.mock(Produto.class);
        when(mockProduto.getId()).thenReturn(id);
        when(mockProduto.getDeletadoEm()).thenReturn(Optional.empty());
        when(produtoRepository.buscarPorIds(anyList())).thenReturn(List.of(mockProduto));

        validator.validar(List.of(new ProdutoSugerido(id, BigDecimal.ONE)));
    }

    @Test
    @DisplayName("Deve lançar erro quando produto não é encontrado")
    void deveLancarErroProdutoNaoEncontrado() {
        UUID id = UUID.randomUUID();
        when(produtoRepository.buscarPorIds(anyList())).thenReturn(List.of());

        assertThrows(IllegalArgumentException.class, () -> 
            validator.validar(List.of(new ProdutoSugerido(id, BigDecimal.ONE)))
        );
    }

    @Test
    @DisplayName("Deve lançar erro quando produto está inativo")
    void deveLancarErroProdutoInativo() {
        UUID id = UUID.randomUUID();
        Produto mockProduto = Mockito.mock(Produto.class);
        when(mockProduto.getId()).thenReturn(id);
        when(mockProduto.getNome()).thenReturn("Peça");
        when(mockProduto.getDeletadoEm()).thenReturn(Optional.of(LocalDateTime.now()));
        when(produtoRepository.buscarPorIds(anyList())).thenReturn(List.of(mockProduto));

        assertThrows(IllegalArgumentException.class, () -> 
            validator.validar(List.of(new ProdutoSugerido(id, BigDecimal.ONE)))
        );
    }
}









