package br.com.fiap.oficina.api.modules.faturamento.application.usecase.comandos;

import br.com.fiap.oficina.api.modules.faturamento.application.event.FaturaPagaEvent;
import br.com.fiap.oficina.api.modules.faturamento.application.repository.FaturaRepository;
import br.com.fiap.oficina.api.modules.faturamento.domain.entity.Fatura;
import br.com.fiap.oficina.api.modules.faturamento.domain.valueObject.MetodoPagamento;
import br.com.fiap.oficina.api.modules.faturamento.domain.valueObject.StatusFatura;
import io.vertx.core.eventbus.EventBus;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfirmarPagamentoFaturaUseCaseTest {

    @Mock
    private FaturaRepository repository;

    @Mock
    private EventBus eventBus;

    @InjectMocks
    private ConfirmarPagamentoFaturaUseCaseImpl useCase;

    @Test
    @DisplayName("Deve confirmar pagamento, atualizar repositório e publicar evento")
    void deveConfirmarPagamento() {
        UUID faturaId = UUID.randomUUID();
        UUID osId = UUID.randomUUID();
        Fatura fatura = new Fatura(faturaId, null, osId, null,
                new BigDecimal("400.00"), StatusFatura.PENDENTE, null);

        when(repository.buscarPorId(faturaId)).thenReturn(Optional.of(fatura));

        useCase.execute(faturaId, MetodoPagamento.PIX);

        assertEquals(StatusFatura.PAGO, fatura.getStatus());
        assertEquals(MetodoPagamento.PIX, fatura.getMetodoPagamento());
        verify(repository).atualizar(fatura);
        verify(eventBus).publish(eq(FaturaPagaEvent.TOPICO), any());
    }

    @Test
    @DisplayName("Deve lançar NotFoundException quando fatura não for encontrada")
    void deveLancarExcecaoQuandoFaturaNaoEncontrada() {
        UUID faturaId = UUID.randomUUID();
        when(repository.buscarPorId(faturaId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> useCase.execute(faturaId, MetodoPagamento.DINHEIRO));
        verify(repository, never()).atualizar(any());
        verifyNoInteractions(eventBus);
    }

    @Test
    @DisplayName("Deve lançar exceção quando método de pagamento for nulo")
    void deveLancarExcecaoQuandoMetodoPagamentoNulo() {
        UUID faturaId = UUID.randomUUID();
        UUID osId = UUID.randomUUID();
        Fatura fatura = new Fatura(faturaId, null, osId, null,
                new BigDecimal("400.00"), StatusFatura.PENDENTE, null);

        when(repository.buscarPorId(faturaId)).thenReturn(Optional.of(fatura));

        assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(faturaId, null));
    }
}
