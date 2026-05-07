package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.ProdutoSnapshotDTO;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface CatalogoProdutoGateway {
    Optional<ProdutoSnapshotDTO> buscarPorId(UUID id);
    void reservarEstoque(UUID id, BigDecimal quantidade);
    void liberarEstoqueReservado(UUID id, BigDecimal quantidade);
    void confirmarVenda(UUID id, BigDecimal quantidade);
}






