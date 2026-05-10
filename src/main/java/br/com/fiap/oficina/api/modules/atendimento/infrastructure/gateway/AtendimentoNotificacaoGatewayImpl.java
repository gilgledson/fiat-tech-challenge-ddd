package br.com.fiap.oficina.api.modules.atendimento.infrastructure.gateway;

import java.util.Optional;
import java.util.UUID;
import br.com.fiap.oficina.api.modules.atendimento.cliente.application.repository.ClienteRepository;
import br.com.fiap.oficina.api.modules.notificacao.application.dto.ClienteSnapshotDTO;
import br.com.fiap.oficina.api.modules.notificacao.application.gateway.AtendimentoNotificacaoGateway;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class AtendimentoNotificacaoGatewayImpl implements AtendimentoNotificacaoGateway {

    private final ClienteRepository clienteRepository;

    @Override
    public Optional<ClienteSnapshotDTO> buscarClientePorId(UUID clienteId) {
        return clienteRepository.buscarPorId(clienteId)
            .map(c -> new ClienteSnapshotDTO(
                c.getId(),
                c.getUsuarioId(),
                c.getEmail()
            ));
    }
}
