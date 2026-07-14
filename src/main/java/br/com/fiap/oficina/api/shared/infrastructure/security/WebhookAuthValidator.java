package br.com.fiap.oficina.api.shared.infrastructure.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Valida o secret compartilhado usado por endpoints webhook (ex: notificação
 * externa de aprovação de orçamento), que não passam pelo fluxo normal de
 * autenticação JWT por serem chamados por sistemas de terceiros.
 */
@ApplicationScoped
public class WebhookAuthValidator {

    private final String secretConfigurado;

    public WebhookAuthValidator(@ConfigProperty(name = "oficina.webhook.aprovacao-secret") String secretConfigurado) {
        this.secretConfigurado = secretConfigurado;
    }

    public boolean valido(String secretRecebido) {
        if (secretRecebido == null || secretRecebido.isBlank()
                || secretConfigurado == null || secretConfigurado.isBlank()) {
            return false;
        }
        return MessageDigest.isEqual(
                secretRecebido.getBytes(StandardCharsets.UTF_8),
                secretConfigurado.getBytes(StandardCharsets.UTF_8));
    }
}
