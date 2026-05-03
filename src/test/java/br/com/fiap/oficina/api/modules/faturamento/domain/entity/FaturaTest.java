package br.com.fiap.oficina.api.modules.faturamento.domain.entity;

import br.com.fiap.oficina.api.modules.faturamento.domain.valueObject.MetodoPagamento;
import br.com.fiap.oficina.api.modules.faturamento.domain.valueObject.StatusFatura;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FaturaTest {

    @Test
    @DisplayName("Deve criar fatura com construtor simples com valores padrão")
    void deveCriarFaturaComValoresPadrao() {
        UUID osId = UUID.randomUUID();
        BigDecimal valor = new BigDecimal("500.00");

        Fatura fatura = new Fatura(osId, valor, MetodoPagamento.PIX);

        assertNotNull(fatura.getId());
        assertEquals(osId, fatura.getOrdemServicoId());
        assertEquals(valor, fatura.getValorTotal());
        assertEquals(MetodoPagamento.PIX, fatura.getMetodoPagamento());
        assertEquals(StatusFatura.PENDENTE, fatura.getStatus());
        assertNotNull(fatura.getDataEmissao());
        assertNotNull(fatura.getDataVencimento());
        assertEquals(fatura.getDataEmissao().plusDays(30), fatura.getDataVencimento());
    }

    @Test
    @DisplayName("Deve criar fatura via construtor completo preservando todos os campos")
    void deveCriarFaturaComConstrutorCompleto() {
        UUID id = UUID.randomUUID();
        UUID osId = UUID.randomUUID();
        LocalDate emissao = LocalDate.of(2025, 1, 1);
        LocalDate vencimento = LocalDate.of(2025, 1, 31);
        BigDecimal valor = new BigDecimal("1000.00");

        Fatura fatura = new Fatura(id, emissao, osId, vencimento, valor, StatusFatura.PAGO, MetodoPagamento.CARTAO_CREDITO);

        assertEquals(id, fatura.getId());
        assertEquals(osId, fatura.getOrdemServicoId());
        assertEquals(emissao, fatura.getDataEmissao());
        assertEquals(vencimento, fatura.getDataVencimento());
        assertEquals(valor, fatura.getValorTotal());
        assertEquals(StatusFatura.PAGO, fatura.getStatus());
        assertEquals(MetodoPagamento.CARTAO_CREDITO, fatura.getMetodoPagamento());
    }

    @Test
    @DisplayName("Deve criar fatura via reconstruir com todos os campos")
    void deveCriarFaturaViaReconstruir() {
        UUID id = UUID.randomUUID();
        UUID osId = UUID.randomUUID();
        LocalDate emissao = LocalDate.now();
        LocalDate vencimento = emissao.plusDays(30);
        BigDecimal valor = new BigDecimal("750.00");

        Fatura fatura = Fatura.reconstruir(id, emissao, osId, vencimento, valor, StatusFatura.PENDENTE, MetodoPagamento.DINHEIRO);

        assertEquals(id, fatura.getId());
        assertEquals(osId, fatura.getOrdemServicoId());
        assertEquals(StatusFatura.PENDENTE, fatura.getStatus());
        assertEquals(MetodoPagamento.DINHEIRO, fatura.getMetodoPagamento());
    }

    @Test
    @DisplayName("Deve validar fatura sem erros quando todos os campos estão preenchidos")
    void deveValidarFaturaSemErros() {
        Fatura fatura = new Fatura(UUID.randomUUID(), new BigDecimal("200.00"), MetodoPagamento.PIX);
        assertDoesNotThrow(fatura::validar);
    }

    @Test
    @DisplayName("Deve lançar exceção na validação quando ordemServicoId for nulo")
    void deveLancarExcecaoQuandoOrdemServicoIdNulo() {
        Fatura fatura = new Fatura((UUID) null, new BigDecimal("200.00"), MetodoPagamento.PIX);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, fatura::validar);
        assertTrue(ex.getMessage().contains("Ordem de serviço"));
    }

    @Test
    @DisplayName("Deve lançar exceção na validação quando dataEmissao for nula")
    void deveLancarExcecaoQuandoDataEmissaoNula() {
        Fatura fatura = new Fatura(UUID.randomUUID(), new BigDecimal("200.00"), MetodoPagamento.PIX);
        fatura.setDataEmissao(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, fatura::validar);
        assertTrue(ex.getMessage().contains("Data de emissão"));
    }

    @Test
    @DisplayName("Deve lançar exceção na validação quando dataVencimento for nula")
    void deveLancarExcecaoQuandoDataVencimentoNula() {
        Fatura fatura = new Fatura(UUID.randomUUID(), new BigDecimal("200.00"), MetodoPagamento.PIX);
        fatura.setDataVencimento(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, fatura::validar);
        assertTrue(ex.getMessage().contains("Data de vencimento"));
    }

    @Test
    @DisplayName("Deve lançar exceção na validação quando valorTotal for nulo")
    void deveLancarExcecaoQuandoValorTotalNulo() {
        Fatura fatura = new Fatura(UUID.randomUUID(), new BigDecimal("200.00"), MetodoPagamento.PIX);
        fatura.setValorTotal(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, fatura::validar);
        assertTrue(ex.getMessage().contains("Valor total"));
    }

    @Test
    @DisplayName("Deve lançar exceção na validação quando status for nulo")
    void deveLancarExcecaoQuandoStatusNulo() {
        Fatura fatura = new Fatura(UUID.randomUUID(), new BigDecimal("200.00"), MetodoPagamento.PIX);
        fatura.setStatus(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, fatura::validar);
        assertTrue(ex.getMessage().contains("Status"));
    }

    @Test
    @DisplayName("Deve cancelar fatura alterando status para CANCELADA")
    void deveCancelarFatura() {
        Fatura fatura = new Fatura(UUID.randomUUID(), new BigDecimal("300.00"), MetodoPagamento.DINHEIRO);
        fatura.cancelar();
        assertEquals(StatusFatura.CANCELADA, fatura.getStatus());
    }

    @Test
    @DisplayName("Deve pagar fatura alterando status para PAGO e gravando método de pagamento")
    void devePagarFatura() {
        Fatura fatura = new Fatura(UUID.randomUUID(), new BigDecimal("300.00"), MetodoPagamento.DINHEIRO);
        fatura.pagar(MetodoPagamento.CARTAO_DEBITO);
        assertEquals(StatusFatura.PAGO, fatura.getStatus());
        assertEquals(MetodoPagamento.CARTAO_DEBITO, fatura.getMetodoPagamento());
    }

    @Test
    @DisplayName("Deve lançar exceção ao pagar fatura com método de pagamento nulo")
    void deveLancarExcecaoAoPagarComMetodoNulo() {
        Fatura fatura = new Fatura(UUID.randomUUID(), new BigDecimal("300.00"), MetodoPagamento.DINHEIRO);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> fatura.pagar(null));
        assertTrue(ex.getMessage().contains("Método de pagamento"));
    }

    @Test
    @DisplayName("Deve marcar fatura como pendente")
    void deveMarcarFaturaComoPendente() {
        Fatura fatura = new Fatura(UUID.randomUUID(), new BigDecimal("300.00"), MetodoPagamento.PIX);
        fatura.cancelar();
        assertEquals(StatusFatura.CANCELADA, fatura.getStatus());
        fatura.pendente();
        assertEquals(StatusFatura.PENDENTE, fatura.getStatus());
    }

    @Test
    @DisplayName("Deve atualizar todos os campos da fatura")
    void deveAtualizarFatura() {
        Fatura fatura = new Fatura(UUID.randomUUID(), new BigDecimal("300.00"), MetodoPagamento.PIX);
        UUID novoOsId = UUID.randomUUID();
        LocalDate novaEmissao = LocalDate.of(2025, 3, 1);
        LocalDate novoVencimento = LocalDate.of(2025, 3, 31);
        BigDecimal novoValor = new BigDecimal("999.99");

        fatura.atualizar(novoOsId, novaEmissao, novoVencimento, novoValor, StatusFatura.PAGO, MetodoPagamento.CARTAO_CREDITO);

        assertEquals(novoOsId, fatura.getOrdemServicoId());
        assertEquals(novaEmissao, fatura.getDataEmissao());
        assertEquals(novoVencimento, fatura.getDataVencimento());
        assertEquals(novoValor, fatura.getValorTotal());
        assertEquals(StatusFatura.PAGO, fatura.getStatus());
        assertEquals(MetodoPagamento.CARTAO_CREDITO, fatura.getMetodoPagamento());
    }
}
