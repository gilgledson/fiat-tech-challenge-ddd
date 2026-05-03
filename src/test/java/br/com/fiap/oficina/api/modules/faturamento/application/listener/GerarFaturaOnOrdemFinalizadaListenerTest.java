package br.com.fiap.oficina.api.modules.faturamento.application.listener;

import br.com.fiap.oficina.api.modules.faturamento.application.usecase.comandos.CriarFaturaUseCase;
import br.com.fiap.oficina.api.modules.faturamento.domain.entity.Fatura;
import br.com.fiap.oficina.api.modules.faturamento.domain.valueObject.MetodoPagamento;
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
    private CriarFaturaUseCase criarFaturaUseCase;

    @InjectMocks
    private GerarFaturaOnOrdemFinalizadaListener listener;

    @Test
    @DisplayName("Deve gerar fatura ao receber evento de execução concluída sem método de pagamento")
    void deveGerarFaturaSemMetodoPagamento() {
        UUID osId = UUID.randomUUID();
        JsonObject json = new JsonObject()
                .put("ordemServicoId", osId.toString())
                .put("valorTotal", 500.0);

        listener.onOrdemServicoFinalizada(json);

        ArgumentCaptor<Fatura> captor = ArgumentCaptor.forClass(Fatura.class);
        verify(criarFaturaUseCase).execute(captor.capture());

        Fatura fatura = captor.getValue();
        assertEquals(osId, fatura.getOrdemServicoId());
        assertNull(fatura.getMetodoPagamento());
    }

    @Test
    @DisplayName("Deve gerar fatura ao receber evento com método de pagamento")
    void deveGerarFaturaComMetodoPagamento() {
        UUID osId = UUID.randomUUID();
        JsonObject json = new JsonObject()
                .put("ordemServicoId", osId.toString())
                .put("valorTotal", 300.0)
                .put("metodoPagamento", "PIX");

        listener.onOrdemServicoFinalizada(json);

        ArgumentCaptor<Fatura> captor = ArgumentCaptor.forClass(Fatura.class);
        verify(criarFaturaUseCase).execute(captor.capture());

        assertEquals(MetodoPagamento.PIX, captor.getValue().getMetodoPagamento());
    }
}
