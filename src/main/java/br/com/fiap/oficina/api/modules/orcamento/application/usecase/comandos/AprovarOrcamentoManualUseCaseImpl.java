package br.com.fiap.oficina.api.modules.orcamento.application.usecase.comandos;

import br.com.fiap.oficina.api.modules.orcamento.application.event.OrcamentoAceitoEvent;
import br.com.fiap.oficina.api.modules.orcamento.application.event.OrcamentoRejeitadoEvent;
import br.com.fiap.oficina.api.modules.orcamento.application.repository.OrcamentoRepository;
import br.com.fiap.oficina.api.modules.orcamento.domain.entity.Orcamento;
import io.vertx.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class AprovarOrcamentoManualUseCaseImpl implements AprovarOrcamentoManualUseCase {

    private final OrcamentoRepository repository;
    private final EventBus eventBus;

    @Override
    @Transactional
    public void executar(UUID orcamentoId, String assinaturaUrl, List<UUID> servicosAceitos,
            List<UUID> servicosRejeitados) {
        Orcamento orcamento = repository.buscarPorId(orcamentoId)
                .orElseThrow(() -> new NotFoundException("Orcamento não encontrada"));

        orcamento.registrarAceiteManual(assinaturaUrl);
        repository.atualizar(orcamento);

        if (servicosAceitos.isEmpty()) {
            OrcamentoRejeitadoEvent rejectedEvent = new OrcamentoRejeitadoEvent(orcamento.getId(),
                    orcamento.getOrdemServicoId());
            System.out.println("📢 [orcamento] Publicando evento de rejeição manual: Orcamento " + orcamento.getId());
            eventBus.publish(OrcamentoRejeitadoEvent.TOPICO, rejectedEvent.toJson());
        } else {
            OrcamentoAceitoEvent acceptedEvent = new OrcamentoAceitoEvent(orcamento.getId(),
                    orcamento.getOrdemServicoId(), servicosAceitos, servicosRejeitados);
            System.out.println("📢 [orcamento] Publicando evento de aceite manual: Orcamento " + orcamento.getId()
                    + " com " + servicosAceitos.size() + " aprovados e " + servicosRejeitados.size() + " rejeitados.");
            eventBus.publish(OrcamentoAceitoEvent.TOPICO, acceptedEvent.toJson());
        }
    }
}
