package br.com.fiap.oficina.api.modules.catalogo.servico.domain;

import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.Servico;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Serviço - Testes de Domínio")
class ServicoTest {

    @Test
    @DisplayName("Deve criar um serviço válido")
    void deveCriarServicoValido() {
        Servico servico = new Servico(
                "Troca de Óleo",
                TipoServico.PREVENTIVO,
                new BigDecimal("150.00"),
                new ArrayList<>());

        assertNotNull(servico.getId());
        assertEquals("Troca de Óleo", servico.getNome());
        assertEquals(TipoServico.PREVENTIVO, servico.getTipo());
        assertEquals(new BigDecimal("150.00"), servico.getPrecoBase());
        assertNull(servico.getDeletadoEm());
        assertTrue(servico.getProdutosSugeridos().isEmpty());
    }

    @Test
    @DisplayName("Deve atualizar um serviço")
    void deveAtualizarServico() {
        Servico servico = new Servico("Nome Original", TipoServico.CORRETIVO, new BigDecimal("100.00"), null);

        servico.atualizar("Nome Novo", TipoServico.PREVENTIVO, new BigDecimal("200.00"), null);

        assertEquals("Nome Novo", servico.getNome());
        assertEquals(TipoServico.PREVENTIVO, servico.getTipo());
        assertEquals(new BigDecimal("200.00"), servico.getPrecoBase());
    }

    @Test
    @DisplayName("Não deve criar serviço com nome vazio")
    void naoDeveCriarServicoComNomeVazio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Servico("", TipoServico.PREVENTIVO, new BigDecimal("10.00"), null));
    }

    @Test
    @DisplayName("Não deve criar serviço com preço base zero ou negativo")
    void naoDeveCriarServicoComPrecoInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> new Servico("Teste", TipoServico.PREVENTIVO, BigDecimal.ZERO, null));
        assertThrows(IllegalArgumentException.class,
                () -> new Servico("Teste", TipoServico.PREVENTIVO, new BigDecimal("-1.00"), null));
    }

    @Test
    @DisplayName("Deve inativar um serviço")
    void deveInativarServico() {
        Servico servico = new Servico("Teste", TipoServico.PREVENTIVO, new BigDecimal("10.00"), null);
        servico.inativar();
        assertNotNull(servico.getDeletadoEm());
    }

    @Test
    @DisplayName("Não deve inativar serviço já inativo")
    void naoDeveInativarServicoJaInativo() {
        Servico servico = new Servico("Teste", TipoServico.PREVENTIVO, new BigDecimal("10.00"), null);
        servico.inativar();
        assertThrows(IllegalArgumentException.class, servico::inativar);
    }

    @Test
    @DisplayName("Deve ativar um serviço inativo")
    void deveAtivarServico() {
        Servico servico = new Servico("Teste", TipoServico.PREVENTIVO, new BigDecimal("10.00"), null);
        servico.inativar();
        servico.ativar();
        assertNull(servico.getDeletadoEm());
    }

    @Test
    @DisplayName("Não deve ativar serviço já ativo")
    void naoDeveAtivarServicoJaAtivo() {
        Servico servico = new Servico("Teste", TipoServico.PREVENTIVO, new BigDecimal("10.00"), null);
        assertThrows(IllegalArgumentException.class, servico::ativar);
    }

    @Test
    @DisplayName("Deve reconstituir um serviço")
    void deveReconstituirServico() {
        UUID id = UUID.randomUUID();
        Servico servico = Servico.reconstituir(id, "Teste", TipoServico.PREVENTIVO, BigDecimal.TEN, null,
                new ArrayList<>());

        assertEquals(id, servico.getId());
        assertEquals("Teste", servico.getNome());
    }
}









