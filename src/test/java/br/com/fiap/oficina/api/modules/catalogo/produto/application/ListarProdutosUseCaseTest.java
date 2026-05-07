package br.com.fiap.oficina.api.modules.catalogo.produto.application;

import br.com.fiap.oficina.api.modules.catalogo.produto.application.repository.ProdutoRepository;
import br.com.fiap.oficina.api.modules.catalogo.produto.application.usecase.ListarProdutosUseCaseImpl;
import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.Produto;
import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.UnidadeMedida;
import br.com.fiap.oficina.api.shared.domain.entity.Pagina;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@DisplayName("Listar Produtos - Testes Unitários")
class ListarProdutosUseCaseTest {

    private ProdutoRepository repository;
    private ListarProdutosUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(ProdutoRepository.class);
        useCase = new ListarProdutosUseCaseImpl(repository);
    }

    @Test
    @DisplayName("Deve listar produtos paginados")
    void deveListarProdutos() {
        Produto produto = new Produto("Oleo", "123456", BigDecimal.TEN, BigDecimal.TEN, BigDecimal.ZERO, UnidadeMedida.LT);
        Pagina<Produto> paginaMock = new Pagina<>(List.of(produto), 0, 10, 1, 1);
        
        when(repository.listarTodos(anyInt(), anyInt(), anyBoolean())).thenReturn(paginaMock);

        Pagina<Produto> resultado = useCase.executar(0, 10, false);

        assertEquals(1, resultado.itens().size());
        assertEquals("Oleo", resultado.itens().get(0).getNome());
    }
}









