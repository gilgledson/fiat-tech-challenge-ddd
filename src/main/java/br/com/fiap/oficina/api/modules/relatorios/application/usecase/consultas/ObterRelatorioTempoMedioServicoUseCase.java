package br.com.fiap.oficina.api.modules.relatorios.application.usecase.consultas;

import java.util.List;

import br.com.fiap.oficina.api.modules.relatorios.api.dto.RelatorioTempoMedioServicoResponse;

public interface ObterRelatorioTempoMedioServicoUseCase {
    List<RelatorioTempoMedioServicoResponse> executar();
}






