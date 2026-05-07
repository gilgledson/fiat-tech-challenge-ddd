package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity;

import lombok.Getter;

@Getter
public enum OrdemDeServicoStatus {
    // 1. Início
    ABERTA("Aberta"),

    // 2. Fase de Orçamento (Oficina / Recepção)
    EM_DIAGNOSTICO("Em Diagnóstico"),
    AGUARDANDO_APROVACAO("Aguardando Aprovação"),

    // 3. Fase Operacional (Oficina)
    APROVADA("Aprovada"),
    EM_EXECUCAO("Em Execução"),

    // 4. Fase Financeira e Fechamento (Caixa / Recepção)
    AGUARDANDO_PAGAMENTO("Aguardando Pagamento"), // O mecânico terminou o serviço! Orcamento gerada.
    PAGA("Paga"),                                 // O cliente passou o cartão. Carro liberado.
    ENTREGUE("Entregue"),                         // Cliente pegou a chave e foi embora.

    // 5. Caminhos Alternativos (Tristes)
    REJEITADA("Rejeitada"),
    CANCELADA("Cancelada");

    private final String descricao;

    OrdemDeServicoStatus(String descricao) {
        this.descricao = descricao;
    }

    public boolean isEncerrada() {
        return this == ENTREGUE || this == CANCELADA || this == REJEITADA;
    }

    public static OrdemDeServicoStatus fromString(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }
        try {
            return OrdemDeServicoStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}






