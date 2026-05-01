package br.com.fiap.oficina.api.modules.faturamento.infrastructure.config;

import br.com.fiap.oficina.api.modules.faturamento.application.repository.FaturaRepository;
import br.com.fiap.oficina.api.modules.faturamento.application.usecase.comandos.ConfirmarPagamentoFaturaUseCase;
import br.com.fiap.oficina.api.modules.faturamento.application.usecase.comandos.ConfirmarPagamentoFaturaUseCaseImpl;
import br.com.fiap.oficina.api.modules.faturamento.application.usecase.comandos.CriarFaturaUseCase;
import br.com.fiap.oficina.api.modules.faturamento.application.usecase.comandos.CriarFaturaUseCaseImpl;
import io.vertx.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

public class FaturaConfigBean {

    @Produces
    @ApplicationScoped
    public CriarFaturaUseCase criarFaturaUseCase(FaturaRepository repository) {
        return new CriarFaturaUseCaseImpl(repository);
    }

    @Produces
    @ApplicationScoped
    public ConfirmarPagamentoFaturaUseCase confirmarPagamentoFaturaUseCase(
            FaturaRepository repository,
            EventBus eventBus) {
        return new ConfirmarPagamentoFaturaUseCaseImpl(repository, eventBus);
    }
}
