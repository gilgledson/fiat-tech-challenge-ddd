package br.com.fiap.oficina.api.modules.orcamento.application.listener;

import br.com.fiap.oficina.api.modules.orcamento.application.usecase.comandos.CriarOrcamentoUseCase;
import br.com.fiap.oficina.api.modules.orcamento.domain.entity.Orcamento;
import br.com.fiap.oficina.api.modules.orcamento.domain.valueObject.MetodoPagamento;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GerarFaturaOnOrdemFinalizadaListenerTest {

    @Mock
    private CriarOrcamentoUseCase CriarOrcamentoUseCase;

    @InjectMocks
    private GerarOrcamentoOnOrdemFinalizadaListener listener;

    @Test
    @DisplayName("Deve gerar Orcamento ao receber evento de execução concluída sem método de pagamento")
    void deveGerarFaturaSemMetodoPagamento() {
        UUID osId = UUID.randomUUID();
        JsonObject json = new JsonObject()
                .put("ordemServicoId", osId.toString())
                .put("valorTotal", 500.0);

        listener.onDiagnosticoConcluido(json);

        ArgumentCaptor<Orcamento> captor = ArgumentCaptor.forClass(Orcamento.class);
        verify(CriarOrcamentoUseCase).execute(captor.capture());

        Orcamento orcamento = captor.getValue();
        assertEquals(osId, orcamento.getOrdemServicoId());
        assertNull(orcamento.getMetodoPagamento());
    }

    @Test
    @DisplayName("Deve gerar Orcamento ao receber evento com método de pagamento")
    void deveGerarFaturaComMetodoPagamento() {
        UUID osId = UUID.randomUUID();
        JsonObject json = new JsonObject()
                .put("ordemServicoId", osId.toString())
                .put("valorTotal", 300.0)
                .put("metodoPagamento", "PIX");

        listener.onDiagnosticoConcluido(json);

        ArgumentCaptor<Orcamento> captor = ArgumentCaptor.forClass(Orcamento.class);
        verify(CriarOrcamentoUseCase).execute(captor.capture());

        assertEquals(MetodoPagamento.PIX, captor.getValue().getMetodoPagamento());
    }
}










