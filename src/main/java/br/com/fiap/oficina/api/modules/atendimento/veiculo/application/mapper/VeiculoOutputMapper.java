package br.com.fiap.oficina.api.modules.atendimento.veiculo.application.mapper;

import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.dto.VeiculoOutput;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.domain.entity.Veiculo;

public class VeiculoOutputMapper {

    public static VeiculoOutput toOutput(Veiculo veiculo) {
        if (veiculo == null) return null;

        return new VeiculoOutput(
                veiculo.getId(),
                veiculo.getClienteId(),
                veiculo.getPlaca(),
                veiculo.getMarca(),
                veiculo.getModelo(),
                veiculo.getAno(),
                veiculo.getDeletadoEm().orElse(null)
        );
    }
}






