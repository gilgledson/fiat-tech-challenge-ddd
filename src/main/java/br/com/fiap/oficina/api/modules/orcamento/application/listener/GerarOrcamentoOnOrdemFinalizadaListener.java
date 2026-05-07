package br.com.fiap.oficina.api.modules.orcamento.application.listener;

import br.com.fiap.oficina.api.modules.orcamento.application.usecase.comandos.AtualizarOrcamentoUseCase;
import br.com.fiap.oficina.api.modules.orcamento.application.usecase.comandos.CriarOrcamentoUseCase;
import br.com.fiap.oficina.api.modules.orcamento.domain.entity.Orcamento;
import br.com.fiap.oficina.api.modules.orcamento.domain.valueObject.MetodoPagamento;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.Blocking;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class GerarOrcamentoOnOrdemFinalizadaListener {

    private final CriarOrcamentoUseCase CriarOrcamentoUseCase;
    private final AtualizarOrcamentoUseCase AtualizarOrcamentoUseCase;

    @ConsumeEvent("operacional.ordem-servico.conclusao-diagnostico")
    @Blocking
    public void onDiagnosticoConcluido(JsonObject json) {
        UUID ordemServicoId = UUID.fromString(json.getString("ordemServicoId"));
        BigDecimal valorTotal = BigDecimal.valueOf(json.getDouble("valorTotal"));

        MetodoPagamento metodoPagamento = json.containsKey("metodoPagamento")
                && json.getString("metodoPagamento") != null
                        ? MetodoPagamento.valueOf(json.getString("metodoPagamento"))
                        : null;

        Orcamento orcamento = new Orcamento(ordemServicoId, valorTotal, metodoPagamento);
        CriarOrcamentoUseCase.execute(orcamento);
    }

    @ConsumeEvent("operacional.ordem-servico.execucao-concluida")
    @Blocking
    public void onExecucaoConcluida(JsonObject json) {
        UUID ordemServicoId = UUID.fromString(json.getString("ordemServicoId"));
        BigDecimal valorTotal = BigDecimal.valueOf(json.getDouble("valorTotal"));

        System.out.println(
                "📢 [orcamento] Atualizando Orcamento da OS " + ordemServicoId + " com valor final: " + valorTotal);
        AtualizarOrcamentoUseCase.executar(ordemServicoId, valorTotal);
    }
}


