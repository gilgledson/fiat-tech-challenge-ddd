package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event;

import io.vertx.core.json.JsonObject;
import java.math.BigDecimal;
import java.util.UUID;

public record OrdemServicoExecucaoConcluidaEvent(UUID ordemServicoId, BigDecimal valorTotal, UUID clienteId) {
    public static final String TOPICO = "operacional.ordem-servico.execucao-concluida";

    public JsonObject toJson() {
        return new JsonObject()
                .put("ordemServicoId", ordemServicoId.toString())
                .put("valorTotal", valorTotal.doubleValue())
                .put("clienteId", clienteId.toString());
    }
}






