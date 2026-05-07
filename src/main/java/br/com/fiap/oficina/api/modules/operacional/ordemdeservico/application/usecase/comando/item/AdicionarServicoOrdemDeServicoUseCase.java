package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.item;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.api.dto.AdicionarServicoRequest;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.OrdemDeServicoOutput;
import java.util.UUID;

public interface AdicionarServicoOrdemDeServicoUseCase {
    OrdemDeServicoOutput executar(UUID ordemDeServicoId, AdicionarServicoRequest request);
}






