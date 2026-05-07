package br.com.fiap.oficina.api.modules.atendimento.veiculo.application.usecase;

import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.dto.VeiculoOutput;

import java.util.UUID;

public interface CadastrarVeiculoUseCase {
    VeiculoOutput executar(UUID clienteId, String placa, String marca, String modelo, int ano);
}






