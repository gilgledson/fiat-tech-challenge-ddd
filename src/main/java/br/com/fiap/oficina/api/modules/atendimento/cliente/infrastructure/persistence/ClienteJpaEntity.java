package br.com.fiap.oficina.api.modules.atendimento.cliente.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "CLIENTE")
@Getter
@Setter
public class ClienteJpaEntity {

    @Id
    private UUID id;

    @Column(name = "usuario_id", unique = true)
    private UUID usuarioId;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(nullable = false, length = 120)
    private String email;

    @Column(name = "cpf_cnpj", nullable = false, unique = true, length = 20)
    private String cpfCnpj;

    @Column(length = 20)
    private String telefone;

    @Embedded
    private EnderecoEmbeddable endereco;

    @Column(name = "deletado_em")
    private LocalDateTime deletadoEm;
}






