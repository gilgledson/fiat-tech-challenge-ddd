package br.com.fiap.oficina.api.shared.infrastructure.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WebhookAuthValidatorTest {

    private final WebhookAuthValidator validator = new WebhookAuthValidator("segredo-correto");

    @Test
    @DisplayName("Deve validar quando o secret recebido é igual ao configurado")
    void deveValidarSecretCorreto() {
        assertTrue(validator.valido("segredo-correto"));
    }

    @Test
    @DisplayName("Deve invalidar quando o secret recebido é diferente do configurado")
    void deveInvalidarSecretDiferente() {
        assertFalse(validator.valido("segredo-errado"));
    }

    @Test
    @DisplayName("Deve invalidar quando o secret recebido é nulo")
    void deveInvalidarSecretNulo() {
        assertFalse(validator.valido(null));
    }

    @Test
    @DisplayName("Deve invalidar quando o secret recebido está em branco")
    void deveInvalidarSecretEmBranco() {
        assertFalse(validator.valido("   "));
    }

    @Test
    @DisplayName("Deve invalidar quando o secret configurado é nulo ou em branco")
    void deveInvalidarQuandoSecretConfiguradoAusente() {
        WebhookAuthValidator semSecretConfigurado = new WebhookAuthValidator(null);
        assertFalse(semSecretConfigurado.valido("qualquer-coisa"));

        WebhookAuthValidator secretConfiguradoEmBranco = new WebhookAuthValidator("  ");
        assertFalse(secretConfiguradoEmBranco.valido("qualquer-coisa"));
    }
}
