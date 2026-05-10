package br.com.fiap.oficina.api.modules.identidade.infrastructure.gateway;

import java.util.Optional;
import java.util.UUID;
import br.com.fiap.oficina.api.modules.identidade.application.repository.UsuarioRepository;
import br.com.fiap.oficina.api.modules.notificacao.application.dto.UsuarioSnapshotDTO;
import br.com.fiap.oficina.api.modules.notificacao.application.gateway.IdentidadeNotificacaoGateway;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class IdentidadeNotificacaoGatewayImpl implements IdentidadeNotificacaoGateway {

    private final UsuarioRepository usuarioRepository;

    @Override
    public Optional<UsuarioSnapshotDTO> buscarUsuarioPorId(UUID usuarioId) {
        return usuarioRepository.buscarPorId(usuarioId)
            .map(u -> new UsuarioSnapshotDTO(
                u.getId(),
                u.getEmail(),
                u.getPerfil().name()
            ));
    }
}
