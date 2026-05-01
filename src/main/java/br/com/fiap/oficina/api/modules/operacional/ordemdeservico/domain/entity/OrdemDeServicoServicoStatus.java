package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity;

public enum OrdemDeServicoServicoStatus {

    ABERTA,
    PENDENTE,
    ORCAMENTO,
    AGUARDANDO_APROVACAO,
    APROVADO,
    REPROVADO,
    EM_EXECUCAO,
    FINALIZADA,
    CANCELADO;

    public static OrdemDeServicoServicoStatus fromString(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }
        return OrdemDeServicoServicoStatus.valueOf(status.toUpperCase());
    }
}
