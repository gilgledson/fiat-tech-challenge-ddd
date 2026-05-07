package br.com.fiap.oficina.api.modules.atendimento.veiculo.application.usecase;

import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.repository.VeiculoRepository;

import java.util.UUID;

public class DeletarVeiculoUseCaseImpl implements DeletarVeiculoUseCase {

    private final VeiculoRepository repository;

    public DeletarVeiculoUseCaseImpl(VeiculoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void executar(UUID id) {
        repository.deletar(id);
    }
}






