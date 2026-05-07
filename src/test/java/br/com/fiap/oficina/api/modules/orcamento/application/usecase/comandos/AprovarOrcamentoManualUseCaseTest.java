package br.com.fiap.oficina.api.modules.orcamento.application.usecase.comandos;

import br.com.fiap.oficina.api.modules.orcamento.application.repository.OrcamentoRepository;
import br.com.fiap.oficina.api.modules.orcamento.domain.entity.Orcamento;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AprovarOrcamentoManualUseCaseTest {

    @Mock
    private OrcamentoRepository repository;

    @Mock
    private EventBus eventBus;

    @InjectMocks
    private AprovarOrcamentoManualUseCaseImpl useCase;

    @Test
    @DisplayName("Deve registrar aceite manual, atualizar repositório e publicar evento")
    void deveRegistrarAceiteManual() {
        UUID orcamentoId = UUID.randomUUID();
        UUID osId = UUID.randomUUID();
        String assinaturaUrl = "http://storage.com/assinatura.png";
        List<UUID> aceitos = List.of(UUID.randomUUID());
        List<UUID> rejeitados = List.of(UUID.randomUUID());

        Orcamento orcamento = new Orcamento(orcamentoId, null, osId, null,
                new BigDecimal("500.00"), StatusOrcamento.PENDENTE, null);

        when(repository.buscarPorId(orcamentoId)).thenReturn(Optional.of(orcamento));

        useCase.executar(orcamentoId, assinaturaUrl, aceitos, rejeitados);

        assertEquals(assinaturaUrl, orcamento.getAssinaturaUrl());
        verify(repository).atualizar(orcamento);
        verify(eventBus).publish(eq("orcamento.aceito"), any());
    }

    @Test
    @DisplayName("Deve lançar NotFoundException quando orçamentonão for encontrado")
    void deveLancarExcecaoQuandoOrcamentoNaoEncontrado() {
        UUID orcamentoId = UUID.randomUUID();
        when(repository.buscarPorId(orcamentoId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> useCase.executar(orcamentoId, "url", List.of(), List.of()));

        verify(repository, never()).atualizar(any());
        verifyNoInteractions(eventBus);
    }
}
