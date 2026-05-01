package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.item;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.OrdemDeServicoOutput;
import java.math.BigDecimal;
import java.util.UUID;

public interface AdicionarProdutoOrdemDeServicoUseCase {
    OrdemDeServicoOutput executar(UUID ordemDeServicoId, UUID produtoId, BigDecimal quantidade);
}
