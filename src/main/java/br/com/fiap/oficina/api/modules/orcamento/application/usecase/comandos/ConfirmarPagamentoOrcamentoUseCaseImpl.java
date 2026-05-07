package br.com.fiap.oficina.api.modules.orcamento.application.usecase.comandos;

import br.com.fiap.oficina.api.modules.orcamento.application.event.OrcamentoPagoEvent;
import br.com.fiap.oficina.api.modules.orcamento.application.repository.OrcamentoRepository;
import br.com.fiap.oficina.api.modules.orcamento.domain.entity.Orcamento;
import br.com.fiap.oficina.api.modules.orcamento.domain.valueObject.MetodoPagamento;
import io.vertx.core.eventbus.EventBus;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.UUID;

public class ConfirmarPagamentoOrcamentoUseCaseImpl implements ConfirmarPagamentoOrcamentoUseCase {
    public ConfirmarPagamentoOrcamentoUseCaseImpl(OrcamentoRepository repository, EventBus eventBus) {
        this.repository = repository;
        this.eventBus = eventBus;
    }

    private final OrcamentoRepository repository;
    private final EventBus eventBus;

    @Override
    @Transactional
    public void execute(UUID orcamentoId, MetodoPagamento metodoPagamento) {
        Orcamento orcamento = repository.buscarPorId(orcamentoId)
                .orElseThrow(() -> new NotFoundException("Orcamento não encontrada"));

        orcamento.pagar(metodoPagamento);
        repository.atualizar(orcamento);

        OrcamentoPagoEvent event = new OrcamentoPagoEvent(orcamento.getId(), orcamento.getOrdemServicoId());

        System.out.println("📢 [orcamento] Publicando evento no barramento: Orcamento " + orcamento.getId() + " paga.");

        eventBus.publish(OrcamentoPagoEvent.TOPICO, event.toJson());
    }
}








