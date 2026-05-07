package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.ClienteSnapshotDTO;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.VeiculoSnapshotDTO;
import java.util.Optional;
import java.util.UUID;

public interface AtendimentoGateway {
    Optional<ClienteSnapshotDTO> buscarClientePorId(UUID id);
    Optional<VeiculoSnapshotDTO> buscarVeiculoPorId(UUID id);
}






