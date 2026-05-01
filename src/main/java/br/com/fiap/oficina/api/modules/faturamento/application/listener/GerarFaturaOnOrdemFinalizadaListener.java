package br.com.fiap.oficina.api.modules.faturamento.application.listener;

import br.com.fiap.oficina.api.modules.faturamento.application.usecase.comandos.CriarFaturaUseCase;
import br.com.fiap.oficina.api.modules.faturamento.domain.entity.Fatura;
import br.com.fiap.oficina.api.modules.faturamento.domain.valueObject.MetodoPagamento;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.Blocking;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class GerarFaturaOnOrdemFinalizadaListener {

    private final CriarFaturaUseCase criarFaturaUseCase;

    @ConsumeEvent("operacional.ordem-servico.execucao-concluida")
    @Blocking
    public void onOrdemServicoFinalizada(JsonObject json) {
        UUID ordemServicoId = UUID.fromString(json.getString("ordemServicoId"));
        BigDecimal valorTotal = BigDecimal.valueOf(json.getDouble("valorTotal"));
        
        MetodoPagamento metodoPagamento = json.containsKey("metodoPagamento") && json.getString("metodoPagamento") != null
                ? MetodoPagamento.valueOf(json.getString("metodoPagamento"))
                : null;

        Fatura fatura = new Fatura(ordemServicoId, valorTotal, metodoPagamento);
        criarFaturaUseCase.execute(fatura);
    }
}
