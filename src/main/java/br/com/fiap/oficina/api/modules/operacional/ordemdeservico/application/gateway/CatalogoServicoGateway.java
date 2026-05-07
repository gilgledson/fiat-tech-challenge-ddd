package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.ServicoSnapshotDTO;
import java.util.Optional;
import java.util.UUID;

public interface CatalogoServicoGateway {
    Optional<ServicoSnapshotDTO> buscarPorId(UUID id);
}






