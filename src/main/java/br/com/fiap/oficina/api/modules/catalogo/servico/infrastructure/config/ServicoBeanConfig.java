package br.com.fiap.oficina.api.modules.catalogo.servico.infrastructure.config;

import br.com.fiap.oficina.api.modules.catalogo.produto.application.repository.ProdutoRepository;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.repository.ServicoRepository;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.*;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.mapper.ServicoOutputMapper;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.validator.ProdutoSugeridoValidator;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

@Dependent
public class ServicoBeanConfig {

    @Singleton
    @Produces
    public ListarServicosUseCase listarServicosUseCase(ServicoRepository servicoRepository,
            ServicoOutputMapper servicoOutputMapper) {
        return new ListarServicosUseCaseImpl(servicoRepository, servicoOutputMapper);
    }

    @Singleton
    @Produces
    public ServicoOutputMapper servicoOutputMapper(ProdutoRepository produtoRepository) {
        return new ServicoOutputMapper(produtoRepository);
    }

    @Produces
    @Singleton
    public ProdutoSugeridoValidator produtoSugeridoValidator(ProdutoRepository produtoRepository) {
        return new ProdutoSugeridoValidator(produtoRepository);
    }

    @Produces
    @Singleton
    public CadastrarServicoUseCase cadastrarServicoUseCase(ServicoRepository servicoRepository,
            ProdutoSugeridoValidator produtoSugeridoValidator, ServicoOutputMapper servicoOutputMapper) {
        return new CadastrarServicoUseCaseImpl(servicoRepository, produtoSugeridoValidator, servicoOutputMapper);
    }

    @Produces
    @Singleton
    public EditarServicoUseCase editarServicoUseCase(ServicoRepository servicoRepository,
            ProdutoSugeridoValidator produtoSugeridoValidator, ServicoOutputMapper servicoOutputMapper) {
        return new EditarServicoUseCaseImpl(servicoRepository, servicoOutputMapper, produtoSugeridoValidator);
    }

    @Produces
    @Singleton
    public AtivarServicoUseCase ativarServicoUseCase(ServicoRepository servicoRepository) {
        return new AtivarServicoUseCaseImpl(servicoRepository);
    }

    @Produces
    @Singleton
    public InativarServicoUseCase inativarServicoUseCase(ServicoRepository servicoRepository) {
        return new InativarServicoUseCaseImpl(servicoRepository);
    }

    @Produces
    @Singleton
    public DeletarServicoUseCase deletarServicoUseCase(ServicoRepository servicoRepository) {
        return new DeletarServicoUseCaseImpl(servicoRepository);
    }

}






