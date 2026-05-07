package br.com.fiap.oficina.api.modules.catalogo.produto.infrastructure.config;

import br.com.fiap.oficina.api.modules.catalogo.produto.application.repository.ProdutoRepository;
import br.com.fiap.oficina.api.modules.catalogo.produto.application.usecase.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

@Dependent
public class ProdutoBeanConfig {
    @Produces
    @Singleton
    public CadastrarProdutoUseCase cadastrarProdutoUseCase(
            ProdutoRepository repository) {
        return new CadastrarProdutoUseCaseImpl(repository);
    }

    @Produces
    @Singleton
    public ListarProdutosUseCase listarProdutosUseCase(ProdutoRepository repository) {
        return new ListarProdutosUseCaseImpl(repository);
    }

    @Produces
    @ApplicationScoped
    public EditarProdutoUseCase editarProdutoUseCase(ProdutoRepository repository) {
        return new EditarProdutoUseCaseImpl(repository);
    }

    @Produces
    @ApplicationScoped
    public InativarProdutoUseCase inativarProdutoUseCase(ProdutoRepository repository) {
        return new InativarProdutoUseCaseImpl(repository);
    }

    @Produces
    @ApplicationScoped
    public AtivarProdutoUseCase ativarProdutoUseCase(ProdutoRepository repository) {
        return new AtivarProdutoUseCaseImpl(repository);
    }

    @Produces
    @ApplicationScoped
    public DeletarProdutoUseCase deletarProdutoUseCase(ProdutoRepository repository) {
        return new DeletarProdutoUseCaseImpl(repository);
    }
}






