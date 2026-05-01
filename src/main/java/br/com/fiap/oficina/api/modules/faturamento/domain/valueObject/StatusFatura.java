package br.com.fiap.oficina.api.modules.faturamento.domain.valueObject;

public enum StatusFatura {
    PENDENTE(1, "Pendente"),
    PAGO(2, "Pago"),
    CANCELADA(3, "Cancelada");

    private final int id;
    private final String nome;

    StatusFatura(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public static StatusFatura fromId(int id) {
        for (StatusFatura status : values()) {
            if (status.id == id) {
                return status;
            }
        }
        throw new IllegalArgumentException("ID de status de fatura inválido: " + id);
    }
}
