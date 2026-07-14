package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.infrastructure.config;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway.AtendimentoGateway;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway.CatalogoProdutoGateway;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway.CatalogoServicoGateway;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.ciclovida.*;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.estado.*;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.item.*;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.consulta.*;
import io.vertx.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class OrdemDeServicoBeanConfig {

    @Produces
    @ApplicationScoped
    public AbrirOrdemDeServicoUseCase abrirOrdemDeServicoUseCase(
            OrdemDeServicoRepository repository,
            AtendimentoGateway atendimentoGateway,
            EventBus eventBus,
            AdicionarServicoOrdemDeServicoUseCase adicionarServicoUseCase) {
        return new AbrirOrdemDeServicoUseCaseImpl(repository, atendimentoGateway, eventBus, adicionarServicoUseCase);
    }

    @Produces
    @ApplicationScoped
    public BuscarOrdemDeServicoUseCase buscarOrdemDeServicoUseCase(OrdemDeServicoRepository repository) {
        return new BuscarOrdemDeServicoUseCaseImpl(repository);
    }

    @Produces
    @ApplicationScoped
    public ListarOrdensDeServicoUseCase listarOrdensDeServicoUseCase(OrdemDeServicoRepository repository) {
        return new ListarOrdensDeServicoUseCaseImpl(repository);
    }

    @Produces
    @ApplicationScoped
    public AdicionarServicoOrdemDeServicoUseCase adicionarServicoOrdemDeServicoUseCase(
            OrdemDeServicoRepository osRepository,
            CatalogoServicoGateway servicoGateway,
            CatalogoProdutoGateway produtoGateway,
            EventBus eventBus) {
        return new AdicionarServicoOrdemDeServicoUseCaseImpl(osRepository, servicoGateway, produtoGateway, eventBus);
    }

    @Produces
    @ApplicationScoped
    public RemoverProdutoOrdemDeServicoUseCase removerProdutoOrdemDeServicoUseCase(
            OrdemDeServicoRepository osRepository,
            CatalogoProdutoGateway produtoGateway) {
        return new RemoverProdutoOrdemDeServicoUseCaseImpl(osRepository, produtoGateway);
    }

    @Produces
    @ApplicationScoped
    public RemoverServicoOrdemDeServicoUseCase removerServicoOrdemDeServicoUseCase(
            OrdemDeServicoRepository osRepository,
            CatalogoProdutoGateway produtoGateway) {
        return new RemoverServicoOrdemDeServicoUseCaseImpl(osRepository, produtoGateway);
    }

    @Produces
    @ApplicationScoped
    public IniciarExecucaoServicoUseCase iniciarExecucaoServicoUseCase(OrdemDeServicoRepository repository) {
        return new IniciarExecucaoServicoUseCaseImpl(repository);
    }

    @Produces
    @ApplicationScoped
    public FinalizarExecucaoServicoUseCase finalizarExecucaoServicoUseCase(OrdemDeServicoRepository repository) {
        return new FinalizarExecucaoServicoUseCaseImpl(repository);
    }

    @Produces
    @ApplicationScoped
    public IniciarDiagnosticoUseCase iniciarDiagnosticoUseCase(
            OrdemDeServicoRepository repository,
            EventBus eventBus) {
        return new IniciarDiagnosticoUseCaseImpl(repository, eventBus);
    }

    @Produces
    @ApplicationScoped
    public AprovarOrdemDeServicoUseCase aprovarOrdemDeServicoUseCase(
            OrdemDeServicoRepository repository,
            CatalogoProdutoGateway produtoGateway,
            EventBus eventBus) {
        return new AprovarOrdemDeServicoUseCaseImpl(repository, produtoGateway, eventBus);
    }

    @Produces
    @ApplicationScoped
    public IniciarExecucaoOsUseCase iniciarExecucaoOsUseCase(
            OrdemDeServicoRepository repository,
            EventBus eventBus) {
        return new IniciarExecucaoOsUseCaseImpl(repository, eventBus);
    }

    @Produces
    @ApplicationScoped
    public ConcluirExecucaoOrdemDeServicoUseCase concluirExecucaoOrdemDeServicoUseCase(
            OrdemDeServicoRepository repository,
            CatalogoProdutoGateway produtoGateway,
            EventBus eventBus) {
        return new ConcluirExecucaoOrdemDeServicoUseCaseImpl(repository, produtoGateway, eventBus);
    }

    @Produces
    @ApplicationScoped
    public CancelarOrdemDeServicoUseCase cancelarOrdemDeServicoUseCase(
            OrdemDeServicoRepository repository,
            CatalogoProdutoGateway produtoGateway,
            EventBus eventBus) {
        return new CancelarOrdemDeServicoUseCaseImpl(repository, produtoGateway, eventBus);
    }

    @Produces
    @ApplicationScoped
    public ConcluirDiagnosticoUseCase concluirDiagnosticoUseCase(
            OrdemDeServicoRepository repository,
            EventBus eventBus) {
        return new ConcluirDiagnosticoUseCaseImpl(repository, eventBus);
    }

    @Produces
    @ApplicationScoped
    public RejeitarOrcamentoUseCase rejeitarOrcamentoUseCase(
            OrdemDeServicoRepository repository,
            CatalogoProdutoGateway produtoGateway,
            EventBus eventBus) {
        return new RejeitarOrcamentoUseCaseImpl(repository, produtoGateway, eventBus);
    }

    @Produces
    @ApplicationScoped
    public EntregarVeiculoUseCase entregarVeiculoUseCase(
            OrdemDeServicoRepository repository,
            EventBus eventBus) {
        return new EntregarVeiculoUseCaseImpl(repository, eventBus);
    }

}






