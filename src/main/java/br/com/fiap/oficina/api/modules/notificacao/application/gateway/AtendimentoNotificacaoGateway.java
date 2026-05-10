package br.com.fiap.oficina.api.modules.notificacao.application.gateway;

import java.util.Optional;
import java.util.UUID;
import br.com.fiap.oficina.api.modules.notificacao.application.dto.ClienteSnapshotDTO;

public interface AtendimentoNotificacaoGateway {
    Optional<ClienteSnapshotDTO> buscarClientePorId(UUID clienteId);
}
