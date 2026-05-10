package br.com.fiap.oficina.api.modules.notificacao.infrastructure.gateway;

import br.com.fiap.oficina.api.modules.notificacao.application.gateway.AtendimentoNotificacaoGateway;
import br.com.fiap.oficina.api.modules.notificacao.application.gateway.IdentidadeNotificacaoGateway;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.NotificacaoSnapshotDTO;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway.NotificacaoGateway;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class NotificacaoGatewayImpl implements NotificacaoGateway {

    private final AtendimentoNotificacaoGateway atendimentoGateway;
    private final IdentidadeNotificacaoGateway identidadeGateway;

    @Override
    public void enviarNotificacao(NotificacaoSnapshotDTO snapshot) {
        var cliente = atendimentoGateway.buscarClientePorId(snapshot.clienteId()).orElse(null);
        if (cliente == null) {
            log.info("[Notificacao] Cliente {} nao encontrado no Atendimento", snapshot.clienteId());
            return;
        }

        // Priorizamos o e-mail do cadastro do cliente (Atendimento)
        String email = cliente.email();

        // Se o e-mail do cadastro estiver vazio, tentamos buscar o e-mail da conta de
        // usuário (Identidade)
        if ((email == null || email.isBlank()) && cliente.usuarioId().isPresent()) {
            var usuarioOpt = identidadeGateway.buscarUsuarioPorId(cliente.usuarioId().get());
            if (usuarioOpt.isPresent()) {
                email = usuarioOpt.get().email();
            }
        }

        if (email == null || email.isBlank()) {
            log.info("[Notificacao] E-mail nao disponivel para o cliente {}", snapshot.clienteId());
            return;
        }

        log.info("\n--- ENVIANDO NOTIFICACAO ---\nID Cliente: {}\nDestinatario: {}\nTitulo: {}\nMensagem: {}\n---",
                snapshot.clienteId(),
                email,
                snapshot.titulo(),
                snapshot.mensagem());
    }
}
