package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.listener;

import java.util.List;
import java.util.UUID;

import org.jboss.logging.Logger;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.api.dto.AprovarServicoRequest;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event.EventosExternos;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event.OrdemServicoPaga;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.ciclovida.CancelarOrdemDeServicoUseCase;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.ciclovida.ConcluirExecucaoOrdemDeServicoUseCase;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.estado.AprovarOrdemDeServicoUseCase;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.estado.ConcluirDiagnosticoUseCase;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.estado.IniciarDiagnosticoUseCase;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.estado.IniciarExecucaoOsUseCase;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.estado.RejeitarOrcamentoUseCase;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.item.FinalizarExecucaoServicoUseCase;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.item.IniciarExecucaoServicoUseCase;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicoStatus;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoStatus;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * Passos da Saga do lado do OS Service: reage aos eventos do Billing Service
 * e do Execution Service e aplica a mudança correspondente na OS.
 *
 * O broker pode entregar a mesma mensagem mais de uma vez, então cada método
 * só age se a OS ainda estiver no status que o evento espera; evento repetido
 * ou atrasado é ignorado.
 */
@ApplicationScoped
@RequiredArgsConstructor
public class SagaEventosListener {

    private static final Logger LOG = Logger.getLogger(SagaEventosListener.class);

    private final OrdemDeServicoRepository repository;
    private final IniciarDiagnosticoUseCase iniciarDiagnostico;
    private final ConcluirDiagnosticoUseCase concluirDiagnostico;
    private final AprovarOrdemDeServicoUseCase aprovar;
    private final RejeitarOrcamentoUseCase rejeitar;
    private final IniciarExecucaoOsUseCase iniciarExecucao;
    private final IniciarExecucaoServicoUseCase iniciarServico;
    private final FinalizarExecucaoServicoUseCase finalizarServico;
    private final ConcluirExecucaoOrdemDeServicoUseCase concluirExecucao;
    private final CancelarOrdemDeServicoUseCase cancelar;
    private final EventBus eventBus;

    // ---------------------------------------------------------------- Execution

    /** Mecânico tirou a OS da fila de diagnóstico. */
    public void onDiagnosticoIniciado(JsonObject json) {
        OrdemDeServico ordem = buscar(json);
        if (ordem.getStatus() == OrdemDeServicoStatus.ABERTA) {
            iniciarDiagnostico.executar(ordem.getId());
        } else {
            ignorar("execucao.diagnostico-iniciado", ordem);
        }
    }

    /** Diagnóstico pronto: a OS segue para orçamento (o Billing reage ao evento que sai daqui). */
    public void onDiagnosticoConcluido(JsonObject json) {
        OrdemDeServico ordem = buscar(json);
        if (ordem.getStatus() == OrdemDeServicoStatus.ABERTA
                || ordem.getStatus() == OrdemDeServicoStatus.EM_DIAGNOSTICO) {
            concluirDiagnostico.executar(ordem.getId());
        } else {
            ignorar("execucao.diagnostico-concluido", ordem);
        }
    }

    public void onExecucaoIniciada(JsonObject json) {
        OrdemDeServico ordem = buscar(json);
        if (ordem.getStatus() == OrdemDeServicoStatus.APROVADA) {
            iniciarExecucao.executar(ordem.getId());
        } else {
            ignorar("execucao.iniciada", ordem);
        }
    }

    public void onServicoIniciado(JsonObject json) {
        OrdemDeServico ordem = buscar(json);
        UUID servicoId = UUID.fromString(json.getString("servicoOsId"));
        if (statusDoServico(ordem, servicoId) == OrdemDeServicoServicoStatus.APROVADO) {
            String executor = json.getString("usuarioExecutorId");
            iniciarServico.executar(ordem.getId(), servicoId, executor != null ? UUID.fromString(executor) : null);
        } else {
            ignorar("execucao.servico-iniciado", ordem);
        }
    }

    public void onServicoFinalizado(JsonObject json) {
        OrdemDeServico ordem = buscar(json);
        UUID servicoId = UUID.fromString(json.getString("servicoOsId"));
        if (statusDoServico(ordem, servicoId) == OrdemDeServicoServicoStatus.EM_EXECUCAO) {
            finalizarServico.executar(ordem.getId(), servicoId);
        } else {
            ignorar("execucao.servico-finalizado", ordem);
        }
    }

    /** Execução concluída: baixa o estoque e a OS segue para pagamento. */
    public void onExecucaoConcluida(JsonObject json) {
        OrdemDeServico ordem = buscar(json);
        if (ordem.getStatus() == OrdemDeServicoStatus.EM_EXECUCAO) {
            concluirExecucao.executar(ordem.getId());
        } else {
            ignorar("execucao.concluida", ordem);
        }
    }

    /** Compensação: a oficina não consegue concluir a OS. Cancela e libera o estoque reservado. */
    public void onExecucaoFalhou(JsonObject json) {
        cancelarSeAtiva(json, "execucao.falhou", "Falha na execução: " + json.getString("motivo", "não informado"));
    }

    // ------------------------------------------------------------------ Billing

    /** Cliente aceitou o orçamento: aprova os serviços aceitos e libera o estoque dos rejeitados. */
    public void onOrcamentoAceito(JsonObject json) {
        OrdemDeServico ordem = buscar(json);
        if (ordem.getStatus() != OrdemDeServicoStatus.AGUARDANDO_APROVACAO) {
            ignorar("billing.orcamento-aceito", ordem);
            return;
        }
        List<UUID> aceitos = ids(json, "servicosAceitos");
        List<UUID> rejeitados = ids(json, "servicosRejeitados");
        if (!aceitos.isEmpty() || !rejeitados.isEmpty()) {
            aprovar.executar(ordem.getId(), new AprovarServicoRequest(aceitos, rejeitados));
        }
        eventBus.publish(EventosExternos.ORCAMENTO_ACEITO, json);
    }

    /** Compensação: cliente rejeitou o orçamento. Rejeita a OS e libera o estoque reservado. */
    public void onOrcamentoRejeitado(JsonObject json) {
        OrdemDeServico ordem = buscar(json);
        if (ordem.getStatus().isEncerrada()) {
            ignorar("billing.orcamento-rejeitado", ordem);
            return;
        }
        rejeitar.executar(ordem.getId());
        eventBus.publish(EventosExternos.ORCAMENTO_REJEITADO, json);
    }

    /** Compensação: o Billing não conseguiu gerar o orçamento. Cancela e libera o estoque reservado. */
    public void onOrcamentoFalhou(JsonObject json) {
        cancelarSeAtiva(json, "billing.orcamento-falhou",
                "Falha ao gerar o orçamento: " + json.getString("motivo", "não informado"));
    }

    public void onPagamentoConfirmado(JsonObject json) {
        OrdemDeServico ordem = buscar(json);
        if (ordem.getStatus() != OrdemDeServicoStatus.AGUARDANDO_PAGAMENTO) {
            ignorar("billing.pagamento-confirmado", ordem);
            return;
        }
        ordem.setStatus(OrdemDeServicoStatus.PAGA);
        repository.atualizar(ordem);
        eventBus.publish(OrdemServicoPaga.TOPICO, new OrdemServicoPaga(ordem.getId()).toJson());
        eventBus.publish(EventosExternos.ORCAMENTO_PAGO, json);
    }

    // ------------------------------------------------------------------ apoio

    private void cancelarSeAtiva(JsonObject json, String evento, String motivo) {
        OrdemDeServico ordem = buscar(json);
        if (ordem.getStatus().isEncerrada()) {
            ignorar(evento, ordem);
            return;
        }
        cancelar.executar(ordem.getId(), motivo);
    }

    private OrdemDeServico buscar(JsonObject json) {
        UUID id = UUID.fromString(json.getString("ordemServicoId"));
        return repository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Ordem de serviço não encontrada: " + id));
    }

    private static OrdemDeServicoServicoStatus statusDoServico(OrdemDeServico ordem, UUID servicoId) {
        return ordem.getServicos().stream()
                .filter(s -> s.getId().equals(servicoId))
                .findFirst()
                .map(s -> s.getStatus())
                .orElseThrow(() -> new NotFoundException("Serviço não encontrado nesta ordem: " + servicoId));
    }

    private static List<UUID> ids(JsonObject json, String campo) {
        return json.getJsonArray(campo, new JsonArray()).stream()
                .map(valor -> UUID.fromString(valor.toString()))
                .toList();
    }

    private static void ignorar(String evento, OrdemDeServico ordem) {
        LOG.infof("Evento %s ignorado: OS %s está em %s", evento, ordem.getId(), ordem.getStatus());
    }
}
