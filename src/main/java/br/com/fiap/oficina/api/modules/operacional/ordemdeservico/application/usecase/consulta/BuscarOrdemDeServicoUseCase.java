package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.consulta;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.OrdemDeServicoOutput;

import java.util.UUID;

public interface BuscarOrdemDeServicoUseCase {
    OrdemDeServicoOutput executar(UUID id);
}






