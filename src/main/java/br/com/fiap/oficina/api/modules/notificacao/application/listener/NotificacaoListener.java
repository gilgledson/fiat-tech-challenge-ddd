package br.com.fiap.oficina.api.modules.notificacao.application.listener;

import br.com.fiap.oficina.api.modules.notificacao.domain.entity.Notificacao;

/**
 * Interface para implementar ouvintes de eventos de notificação.
 * Qualquer módulo pode implementar este listener para receber eventos
 * e enviar notificações para diferentes canais.
 */
public interface NotificacaoListener {
    /**
     * Método invocado quando uma notificação precisa ser enviada.
     * Cada implementação decide como enviar a notificação (e-mail, SMS, WebSocket,
     * etc.)
     * 
     * @param notificacao A notificação a ser enviada
     */
    void enviar(Notificacao notificacao);
}
