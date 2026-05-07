package br.com.fiap.oficina.api.modules.atendimento.veiculo.application.usecase;

import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.dto.VeiculoOutput;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.mapper.VeiculoOutputMapper;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.repository.VeiculoRepository;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.domain.entity.Veiculo;
import jakarta.ws.rs.NotFoundException;

import java.util.UUID;

public class EditarVeiculoUseCaseImpl implements EditarVeiculoUseCase {

    private final VeiculoRepository repository;

    public EditarVeiculoUseCaseImpl(VeiculoRepository repository) {
        this.repository = repository;
    }

    @Override
    public VeiculoOutput executar(UUID id, UUID clienteId, String placa, String marca, String modelo, int ano) {
        Veiculo veiculo = repository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Veículo não encontrado."));

        veiculo.atualizarDados(clienteId, placa, marca, modelo, ano);
        repository.atualizar(veiculo);

        return VeiculoOutputMapper.toOutput(veiculo);
    }
}






