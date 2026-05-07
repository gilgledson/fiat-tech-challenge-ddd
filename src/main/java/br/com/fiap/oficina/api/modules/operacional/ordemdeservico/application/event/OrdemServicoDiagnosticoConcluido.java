package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event;

import io.vertx.core.json.JsonObject;
import java.math.BigDecimal;
import java.util.UUID;

public record OrdemServicoDiagnosticoConcluido(UUID ordemServicoId, BigDecimal valorTotal, UUID clienteId) {
    public static final String TOPICO = "operacional.ordem-servico.conclusao-diagnostico";

    public JsonObject toJson() {
        return new JsonObject()
                .put("ordemServicoId", ordemServicoId.toString())
                .put("valorTotal", valorTotal != null ? valorTotal.doubleValue() : 0.0)
                .put("clienteId", clienteId != null ? clienteId.toString() : null);
    }
}






