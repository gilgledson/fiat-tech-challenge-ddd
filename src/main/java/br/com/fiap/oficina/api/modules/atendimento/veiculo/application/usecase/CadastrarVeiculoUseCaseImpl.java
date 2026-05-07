package br.com.fiap.oficina.api.modules.atendimento.veiculo.application.usecase;

import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.dto.VeiculoOutput;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.mapper.VeiculoOutputMapper;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.repository.VeiculoRepository;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.domain.entity.Veiculo;

import java.util.UUID;

public class CadastrarVeiculoUseCaseImpl implements CadastrarVeiculoUseCase {

    private final VeiculoRepository repository;

    public CadastrarVeiculoUseCaseImpl(VeiculoRepository repository) {
        this.repository = repository;
    }

    @Override
    public VeiculoOutput executar(UUID clienteId, String placa, String marca, String modelo, int ano) {
        repository.buscarPorPlaca(placa).ifPresent(v -> {
            throw new IllegalArgumentException("Já existe um veículo cadastrado com esta placa");
        });

        Veiculo veiculo = new Veiculo(clienteId, placa, marca, modelo, ano);
        repository.salvar(veiculo);

        return VeiculoOutputMapper.toOutput(veiculo);
    }
}






