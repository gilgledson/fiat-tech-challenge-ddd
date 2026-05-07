package br.com.fiap.oficina.api.shared.domain;

import br.com.fiap.oficina.api.shared.domain.entity.Pagina;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Página - Testes de Domínio")
class PaginaTest {

    @Test
    @DisplayName("Deve mapear uma página para outro tipo")
    void deveMapearPagina() {
        List<String> itens = List.of("A", "B", "C");
        Pagina<String> paginaOriginal = new Pagina<>(itens, 0, 10, 1, 3);

        Pagina<Integer> paginaMapeada = paginaOriginal.map(String::length);

        assertEquals(3, paginaMapeada.itens().size());
        assertEquals(1, paginaMapeada.itens().get(0));
        assertEquals(0, paginaMapeada.paginaAtual());
        assertEquals(10, paginaMapeada.tamanhoPagina());
        assertEquals(1, paginaMapeada.totalPaginas());
        assertEquals(3, paginaMapeada.totalElementos());
    }
}









