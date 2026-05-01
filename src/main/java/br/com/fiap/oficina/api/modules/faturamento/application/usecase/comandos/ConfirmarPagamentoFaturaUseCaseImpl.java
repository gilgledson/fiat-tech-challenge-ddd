package br.com.fiap.oficina.api.modules.faturamento.application.usecase.comandos;

import br.com.fiap.oficina.api.modules.faturamento.application.event.FaturaPagaEvent;
import br.com.fiap.oficina.api.modules.faturamento.application.repository.FaturaRepository;
import br.com.fiap.oficina.api.modules.faturamento.domain.entity.Fatura;
import br.com.fiap.oficina.api.modules.faturamento.domain.valueObject.MetodoPagamento;
import io.vertx.core.eventbus.EventBus;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.UUID;

public class ConfirmarPagamentoFaturaUseCaseImpl implements ConfirmarPagamentoFaturaUseCase {
    public ConfirmarPagamentoFaturaUseCaseImpl(FaturaRepository repository, EventBus eventBus) {
        this.repository = repository;
        this.eventBus = eventBus;
    }

    private final FaturaRepository repository;
    private final EventBus eventBus;

    @Override
    @Transactional
    public void execute(UUID faturaId, MetodoPagamento metodoPagamento) {
        Fatura fatura = repository.buscarPorId(faturaId)
                .orElseThrow(() -> new NotFoundException("Fatura não encontrada"));

        fatura.pagar(metodoPagamento);
        repository.atualizar(fatura);

        FaturaPagaEvent event = new FaturaPagaEvent(fatura.getId(), fatura.getOrdemServicoId());

        System.out.println("📢 [Faturamento] Publicando evento no barramento: Fatura " + fatura.getId() + " paga.");

        eventBus.publish(FaturaPagaEvent.TOPICO, event.toJson());
    }
}
