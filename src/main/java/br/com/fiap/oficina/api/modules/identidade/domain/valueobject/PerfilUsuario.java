package br.com.fiap.oficina.api.modules.identidade.domain.valueobject;

import lombok.Getter;

@Getter
public enum PerfilUsuario {
    ADMIN(Constants.ADMIN, "Perfil para Administradores"),
    MECANICO(Constants.MECANICO, "Perfil para Mecânicos"),
    ATENDENTE(Constants.ATENDENTE, "Perfil para Atendentes"),
    CLIENTE(Constants.CLIENTE, "Perfil para Clientes");

    private final String nome;
    private final String descricao;

    PerfilUsuario(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public static PerfilUsuario fromString(String perfil) {
        return PerfilUsuario.valueOf(perfil.toUpperCase());
    }

    public static class Constants {
        public static final String ADMIN = "ADMIN";
        public static final String MECANICO = "MECANICO";
        public static final String ATENDENTE = "ATENDENTE";
        public static final String CLIENTE = "CLIENTE";
    }
}






