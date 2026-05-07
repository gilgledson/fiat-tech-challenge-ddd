package br.com.fiap.oficina.api.modules.orcamento.infrastructure.config;

import br.com.fiap.oficina.api.modules.orcamento.application.repository.OrcamentoRepository;
import br.com.fiap.oficina.api.modules.orcamento.application.usecase.comandos.ConfirmarPagamentoOrcamentoUseCase;
import br.com.fiap.oficina.api.modules.orcamento.application.usecase.comandos.ConfirmarPagamentoOrcamentoUseCaseImpl;
import br.com.fiap.oficina.api.modules.orcamento.application.usecase.comandos.CriarOrcamentoUseCase;
import br.com.fiap.oficina.api.modules.orcamento.application.usecase.comandos.CriarOrcamentoUseCaseImpl;
import io.vertx.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

public class OrcamentoConfigBean {

    @Produces
    @ApplicationScoped
    public CriarOrcamentoUseCase criarOrcamentoUseCase(OrcamentoRepository repository) {
        return new CriarOrcamentoUseCaseImpl(repository);
    }

    @Produces
    @ApplicationScoped
    public ConfirmarPagamentoOrcamentoUseCase confirmarPagamentoOrcamentoUseCase(
            OrcamentoRepository repository,
            EventBus eventBus) {
        return new ConfirmarPagamentoOrcamentoUseCaseImpl(repository, eventBus);
    }
}







