package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.listener;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoStatus;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfirmarPagamentoOsonOrcamentoPagoListenerTest {

    @Mock
    private OrdemDeServicoRepository repository;

    @Mock
    private EventBus eventBus;

    @InjectMocks
    private ConfirmarPagamentoOsOnOrcamentoPagoListener listener;

    @Test
    @DisplayName("Deve atualizar OS para PAGA e publicar evento quando recebe Orcamento pago")
    void deveAtualizarOsParaPagaEPublicarEvento() {
        UUID osId = UUID.randomUUID();
        UUID orcamentoId = UUID.randomUUID();

        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.AGUARDANDO_PAGAMENTO);

        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        JsonObject json = new JsonObject()
                .put("ordemServicoId", osId.toString())
                .put("orcamentoId", orcamentoId.toString());

        listener.onOrcamentoPago(json);

        assertEquals(OrdemDeServicoStatus.PAGA, os.getStatus());
        verify(repository).atualizar(os);
        verify(eventBus).publish(anyString(), any());
    }

    @Test
    @DisplayName("Não deve atualizar OS quando status não for AGUARDANDO_PAGAMENTO")
    void naoDeveAtualizarOsQuandoStatusInapropriado() {
        UUID osId = UUID.randomUUID();
        UUID orcamentoId = UUID.randomUUID();

        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.PAGA);

        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        JsonObject json = new JsonObject()
                .put("ordemServicoId", osId.toString())
                .put("orcamentoId", orcamentoId.toString());

        listener.onOrcamentoPago(json);

        assertEquals(OrdemDeServicoStatus.PAGA, os.getStatus());
        verify(repository, never()).atualizar(any());
        verifyNoInteractions(eventBus);
    }

    @Test
    @DisplayName("Deve lançar exceção quando OS não for encontrada")
    void deveLancarExcecaoQuandoOsNaoEncontrada() {
        UUID osId = UUID.randomUUID();
        when(repository.buscarPorId(osId)).thenReturn(Optional.empty());

        JsonObject json = new JsonObject()
                .put("ordemServicoId", osId.toString())
                .put("orcamentoId", UUID.randomUUID().toString());

        assertThrows(RuntimeException.class, () -> listener.onOrcamentoPago(json));
    }
}









