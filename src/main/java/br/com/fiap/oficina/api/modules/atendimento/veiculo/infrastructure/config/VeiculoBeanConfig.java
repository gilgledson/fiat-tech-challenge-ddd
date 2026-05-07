package br.com.fiap.oficina.api.modules.atendimento.veiculo.infrastructure.config;

import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.repository.VeiculoRepository;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.usecase.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class VeiculoBeanConfig {

    @Produces
    public CadastrarVeiculoUseCase cadastrarVeiculoUseCase(VeiculoRepository repository) {
        return new CadastrarVeiculoUseCaseImpl(repository);
    }

    @Produces
    public EditarVeiculoUseCase editarVeiculoUseCase(VeiculoRepository repository) {
        return new EditarVeiculoUseCaseImpl(repository);
    }

    @Produces
    public DeletarVeiculoUseCase deletarVeiculoUseCase(VeiculoRepository repository) {
        return new DeletarVeiculoUseCaseImpl(repository);
    }

    @Produces
    public AtivarVeiculoUseCase ativarVeiculoUseCase(VeiculoRepository repository) {
        return new AtivarVeiculoUseCaseImpl(repository);
    }

    @Produces
    public InativarVeiculoUseCase inativarVeiculoUseCase(VeiculoRepository repository) {
        return new InativarVeiculoUseCaseImpl(repository);
    }

    @Produces
    public ListarVeiculosUseCase listarVeiculosUseCase(VeiculoRepository repository) {
        return new ListarVeiculosUseCaseImpl(repository);
    }
}






