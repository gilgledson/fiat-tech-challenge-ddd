package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.NotificacaoSnapshotDTO;

public interface NotificacaoGateway {
    void enviarNotificacao(NotificacaoSnapshotDTO snapshot);
}
