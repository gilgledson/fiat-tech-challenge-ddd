package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity;

public enum OrdemDeServicoServicoStatus {

    PENDENTE,
    APROVADO,
    EM_EXECUCAO,
    REJEITADO,
    CANCELADO,
    FINALIZADO;

    public static OrdemDeServicoServicoStatus fromString(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }
        try {
            return OrdemDeServicoServicoStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}






