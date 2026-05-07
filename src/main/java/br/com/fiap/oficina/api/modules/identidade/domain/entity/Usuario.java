package br.com.fiap.oficina.api.modules.identidade.domain.entity;

import br.com.fiap.oficina.api.modules.identidade.domain.valueobject.PerfilUsuario;
import java.util.UUID;


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

    public Usuario(UUID id, String email, String senhaHash, PerfilUsuario perfil, boolean ativo) {
        this.id = id;
        this.email = email;
        this.senhaHash = senhaHash;
        this.perfil = perfil;
        this.ativo = ativo;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}






