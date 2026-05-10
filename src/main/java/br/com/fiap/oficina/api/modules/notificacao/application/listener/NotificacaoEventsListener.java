package br.com.fiap.oficina.api.modules.notificacao.application.listener;

import br.com.fiap.oficina.api.modules.notificacao.application.gateway.OperacionalNotificacaoGateway;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.NotificacaoSnapshotDTO;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway.NotificacaoGateway;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event.*;
import br.com.fiap.oficina.api.modules.orcamento.application.event.OrcamentoAceitoEvent;
import br.com.fiap.oficina.api.modules.orcamento.application.event.OrcamentoPagoEvent;
import br.com.fiap.oficina.api.modules.orcamento.application.event.OrcamentoRejeitadoEvent;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.Blocking;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class NotificacaoEventsListener {

    private final NotificacaoGateway notificacaoGateway;
    private final OperacionalNotificacaoGateway operacionalGateway;

    @ConsumeEvent(OrdemServicoDiagnosticoConcluido.TOPICO)
    @Blocking
    public void onDiagnosticoConcluido(JsonObject json) {
        UUID osId = UUID.fromString(json.getString("ordemServicoId"));
        UUID clienteId = UUID.fromString(json.getString("clienteId"));

        notificacaoGateway.enviarNotificacao(new NotificacaoSnapshotDTO(
            clienteId,
            "Diagnóstico Concluído - Orçamento Pronto",
            "O diagnóstico da sua Ordem de Serviço #" + osId + " foi concluído. O orçamento já está disponível para sua aprovação."
        ));
    }

    @ConsumeEvent(OrdemServicoServicoAdicionado.TOPICO)
    @Blocking
    public void onServicoAdicionado(JsonObject json) {
        UUID osId = UUID.fromString(json.getString("ordemServicoId"));
        UUID clienteId = UUID.fromString(json.getString("clienteId"));
        String nomeServico = json.getString("nomeServico");

        notificacaoGateway.enviarNotificacao(new NotificacaoSnapshotDTO(
            clienteId,
            "Novo Serviço Adicionado",
            "Um novo serviço (" + nomeServico + ") foi adicionado à sua Ordem de Serviço #" + osId + ". Por favor, verifique e aprove para iniciarmos a execução."
        ));
    }

    @ConsumeEvent(OrcamentoAceitoEvent.TOPICO)
    @Blocking
    public void onOrcamentoAceito(JsonObject json) {
        UUID osId = UUID.fromString(json.getString("ordemServicoId"));
        UUID clienteId = operacionalGateway.buscarClienteIdPorOsId(osId);

        if (clienteId != null) {
            notificacaoGateway.enviarNotificacao(new NotificacaoSnapshotDTO(
                clienteId,
                "Orçamento Aprovado",
                "Recebemos sua aprovação para a Ordem de Serviço #" + osId + ". O mecânico já foi notificado e iniciará os trabalhos em breve."
            ));
        }
    }

    @ConsumeEvent(OrdemServicoExecucaoConcluidaEvent.TOPICO)
    @Blocking
    public void onExecucaoConcluida(JsonObject json) {
        UUID osId = UUID.fromString(json.getString("ordemServicoId"));
        UUID clienteId = UUID.fromString(json.getString("clienteId"));

        notificacaoGateway.enviarNotificacao(new NotificacaoSnapshotDTO(
            clienteId,
            "Serviços Concluídos",
            "Todos os serviços da sua Ordem de Serviço #" + osId + " foram finalizados. Seu veículo está pronto, aguardando apenas o pagamento para liberação."
        ));
    }

    @ConsumeEvent(OrcamentoPagoEvent.TOPICO)
    @Blocking
    public void onOrcamentoPago(JsonObject json) {
        UUID osId = UUID.fromString(json.getString("ordemServicoId"));
        UUID clienteId = operacionalGateway.buscarClienteIdPorOsId(osId);

        if (clienteId != null) {
            notificacaoGateway.enviarNotificacao(new NotificacaoSnapshotDTO(
                clienteId,
                "Pagamento Confirmado",
                "Confirmamos o pagamento referente à sua Ordem de Serviço #" + osId + ". Seu veículo já pode ser retirado!"
            ));
        }
    }

    @ConsumeEvent(OrdemServicoEntregue.TOPICO)
    @Blocking
    public void onOsEntregue(JsonObject json) {
        UUID osId = UUID.fromString(json.getString("ordemServicoId"));
        UUID clienteId = UUID.fromString(json.getString("clienteId"));

        notificacaoGateway.enviarNotificacao(new NotificacaoSnapshotDTO(
            clienteId,
            "Veículo Entregue",
            "Sua Ordem de Serviço #" + osId + " foi finalizada com a entrega do veículo. Obrigado por escolher nossa oficina!"
        ));
    }

    @ConsumeEvent(OrdemServicoRejeitada.TOPICO)
    @Blocking
    public void onOsRejeitada(JsonObject json) {
        UUID osId = UUID.fromString(json.getString("ordemServicoId"));
        UUID clienteId = operacionalGateway.buscarClienteIdPorOsId(osId);

        if (clienteId != null) {
            notificacaoGateway.enviarNotificacao(new NotificacaoSnapshotDTO(
                clienteId,
                "Orçamento Rejeitado",
                "O orçamento para a sua Ordem de Serviço #" + osId + " foi rejeitado. Caso tenha dúvidas, entre em contato conosco."
            ));
        }
    }

    @ConsumeEvent(OrcamentoRejeitadoEvent.TOPICO)
    @Blocking
    public void onOrcamentoRejeitado(JsonObject json) {
        UUID osId = UUID.fromString(json.getString("ordemServicoId"));
        UUID clienteId = operacionalGateway.buscarClienteIdPorOsId(osId);

        if (clienteId != null) {
            notificacaoGateway.enviarNotificacao(new NotificacaoSnapshotDTO(
                clienteId,
                "Orçamento Rejeitado",
                "Você optou por não prosseguir com os serviços da Ordem de Serviço #" + osId + ". O veículo está disponível para retirada."
            ));
        }
    }
}
