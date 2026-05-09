package br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoTest {

    @Test
    @DisplayName("Deve criar produto válido")
    void deveCriarProdutoValido() {
        Produto produto = new Produto("Filtro", "123", BigDecimal.TEN, BigDecimal.TEN, BigDecimal.ZERO,
                UnidadeMedida.UN);
        assertNotNull(produto.getId());
    }

    @Test
    @DisplayName("Deve validar preço maior que zero")
    void deveValidarPreco() {
        assertThrows(IllegalArgumentException.class,
                () -> new Produto("Filtro", "123", BigDecimal.ZERO, BigDecimal.TEN, BigDecimal.ZERO, UnidadeMedida.UN));
    }

    @Test
    @DisplayName("Deve validar estoque inicial não negativo")
    void deveValidarEstoque() {
        assertThrows(IllegalArgumentException.class, () -> new Produto("Filtro", "123", BigDecimal.TEN,
                BigDecimal.valueOf(-1), BigDecimal.ZERO, UnidadeMedida.UN));
    }

    @Test
    @DisplayName("Deve validar unidade de medida obrigatória")
    void deveValidarUnidade() {
        assertThrows(IllegalArgumentException.class,
                () -> new Produto("Filtro", "123", BigDecimal.TEN, BigDecimal.TEN, BigDecimal.ZERO, null));
    }

    @Test
    @DisplayName("Deve ativar e inativar produto")
    void deveAtivarEInativar() {
        Produto produto = new Produto("Filtro", "123", BigDecimal.TEN, BigDecimal.TEN, BigDecimal.ZERO,
                UnidadeMedida.UN);

        produto.inativar();
        assertTrue(produto.getDeletadoEm().isPresent());

        assertThrows(IllegalArgumentException.class, produto::inativar);

        produto.ativar();
        assertTrue(produto.getDeletadoEm().isEmpty());

        assertThrows(IllegalArgumentException.class, produto::ativar);
    }

    @Test
    @DisplayName("Deve atualizar dados e validar")
    void deveAtualizarDados() {
        Produto produto = new Produto("Filtro", "123", BigDecimal.TEN, BigDecimal.TEN, BigDecimal.ZERO,
                UnidadeMedida.UN);

        produto.atualizarDados("Novo", "456", BigDecimal.valueOf(20), BigDecimal.valueOf(5), BigDecimal.ZERO,
                UnidadeMedida.LT);

        assertEquals("Novo", produto.getNome());
        assertEquals(BigDecimal.valueOf(20), produto.getPrecoUnitario());
    }

    @Test
    @DisplayName("Deve gerenciar estoque físico")
    void deveGerenciarEstoqueFisico() {
        Produto produto = new Produto("Filtro", "123", BigDecimal.TEN, BigDecimal.TEN, BigDecimal.ZERO,
                UnidadeMedida.UN);

        produto.deduzirEstoqueFisico(BigDecimal.valueOf(5));
        assertEquals(BigDecimal.valueOf(5), produto.getQuantidadeEstoqueFisico());

        assertThrows(IllegalArgumentException.class, () -> produto.deduzirEstoqueFisico(BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> produto.deduzirEstoqueFisico(BigDecimal.valueOf(10)));

        produto.adicionarEstoqueFisico(BigDecimal.valueOf(2));
        assertEquals(BigDecimal.valueOf(7), produto.getQuantidadeEstoqueFisico());
        assertThrows(IllegalArgumentException.class, () -> produto.adicionarEstoqueFisico(BigDecimal.ZERO));
    }

    @Test
    @DisplayName("Deve gerenciar reserva de estoque")
    void deveGerenciarReserva() {
        Produto produto = new Produto("Filtro", "123", BigDecimal.TEN, BigDecimal.TEN, BigDecimal.ZERO,
                UnidadeMedida.UN);

        produto.reservarEstoque(BigDecimal.valueOf(3));
        assertEquals(BigDecimal.valueOf(3), produto.getQuantidadeEstoqueReservado());
        assertEquals(BigDecimal.valueOf(7), produto.calcularDisponivel());

        assertThrows(IllegalArgumentException.class, () -> produto.reservarEstoque(BigDecimal.valueOf(10)));

        produto.liberarEstoqueReservado(BigDecimal.valueOf(1));
        assertEquals(BigDecimal.valueOf(2), produto.getQuantidadeEstoqueReservado());

        assertThrows(IllegalArgumentException.class, () -> produto.liberarEstoqueReservado(BigDecimal.valueOf(5)));
    }

    @Test
    @DisplayName("Deve confirmar venda de reserva")
    void deveConfirmarVenda() {
        Produto produto = new Produto("Filtro", "123", BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(5),
                UnidadeMedida.UN);

        produto.confirmarVendaDeReserva(BigDecimal.valueOf(2));
        assertEquals(BigDecimal.valueOf(3), produto.getQuantidadeEstoqueReservado());
        assertEquals(BigDecimal.valueOf(8), produto.getQuantidadeEstoqueFisico());

        assertThrows(IllegalArgumentException.class, () -> produto.confirmarVendaDeReserva(BigDecimal.valueOf(10)));
        assertThrows(IllegalArgumentException.class, () -> produto.confirmarVendaDeReserva(BigDecimal.ZERO));
    }
}
