package br.com.fiap.oficina.api.modules.faturamento.domain.valueObject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusFaturaTest {

    @Test
    @DisplayName("fromId(1) deve retornar PENDENTE")
    void deveRetornarPendenteParaId1() {
        assertEquals(StatusFatura.PENDENTE, StatusFatura.fromId(1));
    }

    @Test
    @DisplayName("fromId(2) deve retornar PAGO")
    void deveRetornarPagoParaId2() {
        assertEquals(StatusFatura.PAGO, StatusFatura.fromId(2));
    }

    @Test
    @DisplayName("fromId(3) deve retornar CANCELADA")
    void deveRetornarCanceladaParaId3() {
        assertEquals(StatusFatura.CANCELADA, StatusFatura.fromId(3));
    }

    @Test
    @DisplayName("fromId com ID inválido deve lançar IllegalArgumentException")
    void deveLancarExcecaoParaIdInvalido() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> StatusFatura.fromId(99));
        assertTrue(ex.getMessage().contains("99"));
    }

    @Test
    @DisplayName("Deve retornar nome e id corretos para cada status")
    void deveRetornarNomeEIdCorretos() {
        assertEquals(1, StatusFatura.PENDENTE.getId());
        assertEquals("Pendente", StatusFatura.PENDENTE.getNome());

        assertEquals(2, StatusFatura.PAGO.getId());
        assertEquals("Pago", StatusFatura.PAGO.getNome());

        assertEquals(3, StatusFatura.CANCELADA.getId());
        assertEquals("Cancelada", StatusFatura.CANCELADA.getNome());
    }
}
