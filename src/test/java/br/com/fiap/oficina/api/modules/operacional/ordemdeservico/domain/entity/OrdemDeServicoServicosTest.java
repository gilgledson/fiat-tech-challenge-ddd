package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity;

import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrdemDeServicoServicosTest {

    @Test
    @DisplayName("Construtor completo com 12 parâmetros deve preservar todos os campos")
    void deveCriarComConstrutorCompleto() {
        UUID id = UUID.randomUUID();
        UUID osId = UUID.randomUUID();
        UUID servicoId = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();
        LocalDateTime inicio = LocalDateTime.now().minusHours(2);
        LocalDateTime fim = LocalDateTime.now();

        OrdemDeServicoServicos servico = new OrdemDeServicoServicos(
                id, osId, servicoId, "Alinhamento", 2,
                new BigDecimal("80.00"), new BigDecimal("160.00"),
                OrdemDeServicoServicoStatus.FINALIZADO, TipoServico.PREVENTIVO,
                inicio, fim, usuarioId);

        assertEquals(id, servico.getId());
        assertEquals(osId, servico.getOrdemDeServicoId());
        assertEquals(servicoId, servico.getServicoId());
        assertEquals("Alinhamento", servico.getNome());
        assertEquals(2, servico.getQuantidade());
        assertEquals(new BigDecimal("80.00"), servico.getPrecoUnitario());
        assertEquals(new BigDecimal("160.00"), servico.getTotal());
        assertEquals(OrdemDeServicoServicoStatus.FINALIZADO, servico.getStatus());
        assertEquals(TipoServico.PREVENTIVO, servico.getTipo());
        assertEquals(inicio, servico.getDataInicioExecucao());
        assertEquals(fim, servico.getDataFimExecucao());
        assertEquals(usuarioId, servico.getUsuarioExecutorId());
    }

    @Test
    @DisplayName("calcularTotal deve somar preço do serviço mais produtos")
    void deveCalcularTotalComProdutos() {
        UUID osId = UUID.randomUUID();
        OrdemDeServicoServicos servico = new OrdemDeServicoServicos(
                osId, UUID.randomUUID(), "Troca de óleo", 1,
                new BigDecimal("100.00"), BigDecimal.ZERO,
                OrdemDeServicoServicoStatus.PENDENTE, TipoServico.PREVENTIVO);

        OrdemDeServicoProdutos produto = new OrdemDeServicoProdutos();
        produto.setPrecoUnitario(new BigDecimal("50.00"));
        produto.setQuantidade(new BigDecimal("2"));
        produto.setTotal(new BigDecimal("100.00"));
        servico.getProdutos().add(produto);

        servico.calcularTotal();

        assertEquals(new BigDecimal("200.00"), servico.getTotal());
    }

    @Test
    @DisplayName("calcularTotal com precoUnitario nulo deve usar apenas total dos produtos")
    void deveCalcularTotalComPrecoNulo() {
        UUID osId = UUID.randomUUID();
        OrdemDeServicoServicos servico = new OrdemDeServicoServicos(
                osId, UUID.randomUUID(), "Serviço", 1,
                null, BigDecimal.ZERO,
                OrdemDeServicoServicoStatus.PENDENTE, TipoServico.CORRETIVO);

        servico.calcularTotal();

        assertEquals(BigDecimal.ZERO, servico.getTotal());
    }

    @Test
    @DisplayName("getTotal deve calcular lazily quando total for nulo")
    void deveCalcularTotalLazily() {
        UUID osId = UUID.randomUUID();
        OrdemDeServicoServicos servico = new OrdemDeServicoServicos(
                osId, UUID.randomUUID(), "Diagnóstico", 2,
                new BigDecimal("75.00"), null,
                OrdemDeServicoServicoStatus.PENDENTE, TipoServico.PREVENTIVO);

        BigDecimal total = servico.getTotal();

        assertEquals(new BigDecimal("150.00"), total);
    }

    @Test
    @DisplayName("getDuracao deve retornar null quando datas forem nulas")
    void deveRetornarNullParaDuracaoSemDatas() {
        UUID osId = UUID.randomUUID();
        OrdemDeServicoServicos servico = new OrdemDeServicoServicos(
                osId, UUID.randomUUID(), "Balanceamento", 1,
                BigDecimal.TEN, BigDecimal.TEN,
                OrdemDeServicoServicoStatus.PENDENTE, TipoServico.PREVENTIVO);

        assertNull(servico.getDuracao());
    }

    @Test
    @DisplayName("getDuracao deve retornar string formatada quando datas estiverem definidas")
    void deveRetornarDuracaoFormatada() {
        UUID id = UUID.randomUUID();
        UUID osId = UUID.randomUUID();
        LocalDateTime inicio = LocalDateTime.of(2025, 1, 1, 8, 0);
        LocalDateTime fim = LocalDateTime.of(2025, 1, 1, 10, 30);

        OrdemDeServicoServicos servico = new OrdemDeServicoServicos(
                id, osId, UUID.randomUUID(), "Revisão", 1,
                BigDecimal.TEN, BigDecimal.TEN,
                OrdemDeServicoServicoStatus.FINALIZADO, TipoServico.PREVENTIVO,
                inicio, fim, null);

        assertEquals("02h 30m", servico.getDuracao());
    }

    @Test
    @DisplayName("OrdemDeServicoStatus.fromString deve converter status válido")
    void deveConverterStatusDaOSPorString() {
        assertEquals(OrdemDeServicoStatus.ABERTA, OrdemDeServicoStatus.fromString("ABERTA"));
        assertEquals(OrdemDeServicoStatus.EM_EXECUCAO, OrdemDeServicoStatus.fromString("em_execucao"));
        assertEquals(OrdemDeServicoStatus.CANCELADA, OrdemDeServicoStatus.fromString("CANCELADA"));
    }

    @Test
    @DisplayName("OrdemDeServicoStatus.fromString deve retornar null para entrada inválida")
    void deveRetornarNullParaStatusInvalidoDaOS() {
        assertNull(OrdemDeServicoStatus.fromString(null));
        assertNull(OrdemDeServicoStatus.fromString("   "));
        assertNull(OrdemDeServicoStatus.fromString("INVALIDO"));
    }

    @Test
    @DisplayName("OrdemDeServicoStatus.isEncerrada deve retornar true para REJEITADA")
    void deveRetornarEncerradaParaRejeitada() {
        assertTrue(OrdemDeServicoStatus.REJEITADA.isEncerrada());
    }

    @Test
    @DisplayName("OrdemDeServicoServicoStatus.fromString deve converter e retornar null para inválido")
    void deveConverterServicoStatusPorString() {
        assertEquals(OrdemDeServicoServicoStatus.PENDENTE, OrdemDeServicoServicoStatus.fromString("PENDENTE"));
        assertEquals(OrdemDeServicoServicoStatus.APROVADO, OrdemDeServicoServicoStatus.fromString("aprovado"));
        assertNull(OrdemDeServicoServicoStatus.fromString(null));
        assertNull(OrdemDeServicoServicoStatus.fromString(""));
        assertNull(OrdemDeServicoServicoStatus.fromString("INEXISTENTE"));
    }
}









