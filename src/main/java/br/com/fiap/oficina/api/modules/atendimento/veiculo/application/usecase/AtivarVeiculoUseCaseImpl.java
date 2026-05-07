package br.com.fiap.oficina.api.modules.atendimento.veiculo.application.usecase;

import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.repository.VeiculoRepository;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.domain.entity.Veiculo;
import jakarta.ws.rs.NotFoundException;

import java.util.UUID;

public class AtivarVeiculoUseCaseImpl implements AtivarVeiculoUseCase {

    private final VeiculoRepository repository;

    public AtivarVeiculoUseCaseImpl(VeiculoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void executar(UUID id) {
        Veiculo veiculo = repository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Veículo não encontrado."));

        veiculo.ativar();
        repository.atualizar(veiculo);
    }
}






