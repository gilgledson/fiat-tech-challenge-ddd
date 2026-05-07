package br.com.fiap.oficina.api.modules.orcamento.application.usecase.comandos;

import br.com.fiap.oficina.api.modules.orcamento.application.event.OrcamentoPagoEvent;
import br.com.fiap.oficina.api.modules.orcamento.application.repository.OrcamentoRepository;
import br.com.fiap.oficina.api.modules.orcamento.domain.entity.Orcamento;
import br.com.fiap.oficina.api.modules.orcamento.domain.valueObject.MetodoPagamento;
import br.com.fiap.oficina.api.modules.orcamento.domain.valueObject.StatusOrcamento;
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
class ConfirmarPagamentoOrcamentoUseCaseTest {

    @Mock
    private OrcamentoRepository repository;

    @Mock
    private EventBus eventBus;

    @InjectMocks
    private ConfirmarPagamentoOrcamentoUseCaseImpl useCase;

    @Test
    @DisplayName("Deve confirmar pagamento, atualizar repositório e publicar evento")
    void deveConfirmarPagamento() {
        UUID orcamentoId = UUID.randomUUID();
        UUID osId = UUID.randomUUID();
        Orcamento orcamento = new Orcamento(orcamentoId, null, osId, null,
                new BigDecimal("400.00"), StatusOrcamento.PENDENTE, null);

        when(repository.buscarPorId(orcamentoId)).thenReturn(Optional.of(orcamento));

        useCase.execute(orcamentoId, MetodoPagamento.PIX);

        assertEquals(StatusOrcamento.PAGO, orcamento.getStatus());
        assertEquals(MetodoPagamento.PIX, orcamento.getMetodoPagamento());
        verify(repository).atualizar(orcamento);
        verify(eventBus).publish(eq(OrcamentoPagoEvent.TOPICO), any());
    }

    @Test
    @DisplayName("Deve lançar NotFoundException quando Orcamento não for encontrada")
    void deveLancarExcecaoQuandoFaturaNaoEncontrada() {
        UUID orcamentoId = UUID.randomUUID();
        when(repository.buscarPorId(orcamentoId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> useCase.execute(orcamentoId, MetodoPagamento.DINHEIRO));
        verify(repository, never()).atualizar(any());
        verifyNoInteractions(eventBus);
    }

    @Test
    @DisplayName("Deve lançar exceção quando método de pagamento for nulo")
    void deveLancarExcecaoQuandoMetodoPagamentoNulo() {
        UUID orcamentoId = UUID.randomUUID();
        UUID osId = UUID.randomUUID();
        Orcamento orcamento = new Orcamento(orcamentoId, null, osId, null,
                new BigDecimal("400.00"), StatusOrcamento.PENDENTE, null);

        when(repository.buscarPorId(orcamentoId)).thenReturn(Optional.of(orcamento));

        assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(orcamentoId, null));
    }
}

