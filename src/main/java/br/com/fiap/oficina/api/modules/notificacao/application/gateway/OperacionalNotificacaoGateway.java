package br.com.fiap.oficina.api.modules.notificacao.application.gateway;

import java.util.UUID;

public interface OperacionalNotificacaoGateway {
    UUID buscarClienteIdPorOsId(UUID osId);
}
