package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.vertx.core.json.JsonObject;

class OrdemServicoAprovadaTest {

    @Test
    @DisplayName("O evento leva os serviços aprovados no formato esperado pelo Execution Service")
    void deveSerializarServicosAprovados() {
        UUID osId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        UUID servicoId = UUID.randomUUID();
        OrdemServicoAprovada evento = new OrdemServicoAprovada(osId,
                List.of(new OrdemServicoAprovada.ServicoAprovado(itemId, servicoId, "Troca de óleo")));

        JsonObject json = evento.toJson();

        assertEquals(osId.toString(), json.getString("ordemServicoId"));
        assertEquals(1, json.getJsonArray("servicos").size());
        JsonObject servico = json.getJsonArray("servicos").getJsonObject(0);
        assertEquals(itemId.toString(), servico.getString("id"));
        assertEquals(servicoId.toString(), servico.getString("servicoId"));
        assertEquals("Troca de óleo", servico.getString("nome"));
    }
}
