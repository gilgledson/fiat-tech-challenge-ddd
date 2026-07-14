package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.ciclovida;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.api.dto.AdicionarServicoRequest;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.OrdemDeServicoOutput;

import java.util.List;
import java.util.UUID;

public interface AbrirOrdemDeServicoUseCase {
    OrdemDeServicoOutput executar(UUID clienteId, UUID veiculoId, String descricaoProblema,
            List<AdicionarServicoRequest> servicos);
}






