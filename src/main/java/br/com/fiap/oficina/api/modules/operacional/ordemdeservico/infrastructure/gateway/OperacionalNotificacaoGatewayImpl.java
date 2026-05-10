package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.infrastructure.gateway;

import br.com.fiap.oficina.api.modules.notificacao.application.gateway.OperacionalNotificacaoGateway;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class OperacionalNotificacaoGatewayImpl implements OperacionalNotificacaoGateway {

    private final OrdemDeServicoRepository repository;

    @Override
    public UUID buscarClienteIdPorOsId(UUID osId) {
        return repository.buscarPorId(osId)
            .map(OrdemDeServico::getClienteId)
            .orElse(null);
    }
}
