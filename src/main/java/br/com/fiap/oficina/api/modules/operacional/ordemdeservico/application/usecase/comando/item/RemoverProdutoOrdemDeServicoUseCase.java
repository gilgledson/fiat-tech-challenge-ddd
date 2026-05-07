package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.item;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.OrdemDeServicoOutput;
import java.util.UUID;

public interface RemoverProdutoOrdemDeServicoUseCase {
    OrdemDeServicoOutput executar(UUID ordemDeServicoId, UUID produtoId);
}






