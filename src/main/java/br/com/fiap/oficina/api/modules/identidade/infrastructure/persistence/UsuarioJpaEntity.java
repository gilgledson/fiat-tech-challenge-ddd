package br.com.fiap.oficina.api.modules.identidade.infrastructure.persistence;

import br.com.fiap.oficina.api.modules.identidade.domain.entity.PerfilUsuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "USUARIO")
@Getter
@Setter
public class UsuarioJpaEntity {
    @Id
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "senha_hash", nullable = false)
    private String senhaHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PerfilUsuario perfil;

    @Column(nullable = false)
    private boolean ativo;
}
