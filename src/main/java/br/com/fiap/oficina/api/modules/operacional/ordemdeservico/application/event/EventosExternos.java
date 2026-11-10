package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event;

/**
 * Tópicos do barramento interno (Vert.x EventBus) em que o OS Service
 * republica eventos recebidos do Billing Service, para os módulos internos
 * que só precisam ser avisados (ex.: notificação ao cliente).
 */
public final class EventosExternos {

    public static final String ORCAMENTO_ACEITO = "orcamento.aceito";
    public static final String ORCAMENTO_PAGO = "orcamento.Orcamento.paga";
    public static final String ORCAMENTO_REJEITADO = "orcamento.rejeitado";

    private EventosExternos() {
    }
}
