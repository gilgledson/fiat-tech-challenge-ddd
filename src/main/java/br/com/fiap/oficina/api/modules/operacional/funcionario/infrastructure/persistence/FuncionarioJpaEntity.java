package br.com.fiap.oficina.api.modules.operacional.funcionario.infrastructure.persistence;

import br.com.fiap.oficina.api.modules.operacional.funcionario.domain.entity.Funcionario;
import br.com.fiap.oficina.api.modules.operacional.funcionario.domain.valueObject.CargoFuncionario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "FUNCIONARIOS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioJpaEntity {
    @Id
    private UUID id;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "sobrenome", nullable = false)
    private String sobrenome;

    @Enumerated(EnumType.STRING)
    @Column(name = "cargo", nullable = false)
    private CargoFuncionario cargo;

    @Column(name = "cpf", nullable = false)
    private String cpf;

    @Column(name = "telefone", nullable = false)
    private String telefone;

    @Column(name = "ativo", nullable = false)
    private boolean ativo;

    @Column(name = "deletado_em")
    private LocalDateTime deletadoEm;

    public static FuncionarioJpaEntity fromDomain(Funcionario funcionario) {
        return new FuncionarioJpaEntity(
                funcionario.getId(),
                funcionario.getUsuarioId(),
                funcionario.getNome(),
                funcionario.getSobrenome(),
                funcionario.getCargo(),
                funcionario.getCpf(),
                funcionario.getTelefone(),
                funcionario.isAtivo(),
                funcionario.getDeletadoEm());
    }

    public Funcionario toDomain() {
        return Funcionario.builder()
                .id(id)
                .usuarioId(usuarioId)
                .nome(nome)
                .sobrenome(sobrenome)
                .cpf(cpf)
                .telefone(telefone)
                .cargo(cargo)
                .ativo(ativo)
                .deletadoEm(deletadoEm)
                .build();
    }
}






