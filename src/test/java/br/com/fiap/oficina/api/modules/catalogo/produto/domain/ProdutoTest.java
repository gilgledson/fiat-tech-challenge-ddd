package br.com.fiap.oficina.api.modules.catalogo.produto.domain;

import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.Produto;
import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.UnidadeMedida;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Produto - Testes de Domínio")
class ProdutoTest {

    private Produto produtoValido() {
        return new Produto("Óleo 5W40", "7891234567890", new BigDecimal("45.90"), new BigDecimal("10"), UnidadeMedida.UN);
    }

    @Test
    @DisplayName("Deve criar produto válido com sucesso")
    void deveCriarProdutoValido() {
        Produto produto = produtoValido();

        assertNotNull(produto.getId());
        assertEquals("Óleo 5W40", produto.getNome());
        assertEquals("7891234567890", produto.getCodigoBarras());
        assertEquals(0, new BigDecimal("45.90").compareTo(produto.getPrecoUnitario()));
        assertTrue(produto.getDeletadoEm().isEmpty());
    }

    @Test
    @DisplayName("Não deve criar produto com preço zero")
    void naoDeveCriarComPrecoZero() {
        assertThrows(IllegalArgumentException.class, () ->
            new Produto("Produto", "123", BigDecimal.ZERO, BigDecimal.TEN, UnidadeMedida.UN)
        );
    }

    @Test
    @DisplayName("Não deve criar produto com preço negativo")
    void naoDeveCriarComPrecoNegativo() {
        assertThrows(IllegalArgumentException.class, () ->
            new Produto("Produto", "123", new BigDecimal("-1"), BigDecimal.TEN, UnidadeMedida.UN)
        );
    }

    @Test
    @DisplayName("Não deve criar produto com estoque negativo")
    void naoDeveCriarComEstoqueNegativo() {
        assertThrows(IllegalArgumentException.class, () ->
            new Produto("Produto", "123", BigDecimal.TEN, new BigDecimal("-5"), UnidadeMedida.UN)
        );
    }

    @Test
    @DisplayName("Não deve criar produto sem unidade de medida")
    void naoDeveCriarSemUnidadeMedida() {
        assertThrows(IllegalArgumentException.class, () ->
            new Produto("Produto", "123", BigDecimal.TEN, BigDecimal.TEN, null)
        );
    }

    @Test
    @DisplayName("reconstituir deve criar produto com dados fornecidos")
    void deveReconstituirProduto() {
        UUID id = UUID.randomUUID();
        LocalDateTime deletadoEm = LocalDateTime.now();
        Produto produto = Produto.reconstituir(id, "Produto", "123", BigDecimal.TEN, BigDecimal.ONE, UnidadeMedida.LT, Optional.of(deletadoEm));

        assertEquals(id, produto.getId());
        assertTrue(produto.getDeletadoEm().isPresent());
    }

    @Test
    @DisplayName("inativar deve marcar deletadoEm")
    void deveInativarProduto() {
        Produto produto = produtoValido();
        produto.inativar();
        assertTrue(produto.getDeletadoEm().isPresent());
    }

    @Test
    @DisplayName("inativar produto já inativo deve lançar exceção")
    void naoDeveInativarProdutoJaInativo() {
        Produto produto = produtoValido();
        produto.inativar();
        assertThrows(IllegalArgumentException.class, produto::inativar);
    }

    @Test
    @DisplayName("ativar deve limpar deletadoEm")
    void deveAtivarProduto() {
        Produto produto = Produto.reconstituir(UUID.randomUUID(), "P", "1", BigDecimal.TEN, BigDecimal.TEN, UnidadeMedida.UN, Optional.of(LocalDateTime.now()));
        produto.ativar();
        assertTrue(produto.getDeletadoEm().isEmpty());
    }

    @Test
    @DisplayName("ativar produto já ativo deve lançar exceção")
    void naoDeveAtivarProdutoJaAtivo() {
        Produto produto = produtoValido();
        assertThrows(IllegalArgumentException.class, produto::ativar);
    }
}
