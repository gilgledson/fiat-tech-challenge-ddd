package br.com.fiap.oficina.api.modules.identidade.domain.entity;

import java.util.UUID;
import lombok.Getter;

@Getter
public class Usuario {
    private UUID id;
    private String email;
    private String senhaHash;
    private PerfilUsuario perfil;
    private boolean ativo;

    public Usuario(String email, String senhaHash, PerfilUsuario perfil) {
        this.id = UUID.randomUUID();
        this.email = email;
        this.senhaHash = senhaHash;
        this.perfil = perfil;
        this.ativo = true;
    }
}
