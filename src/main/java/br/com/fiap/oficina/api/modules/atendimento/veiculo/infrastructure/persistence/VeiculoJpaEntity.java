package br.com.fiap.oficina.api.modules.atendimento.veiculo.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "VEICULO")
@Getter
@Setter
public class VeiculoJpaEntity {

    @Id
    private UUID id;

    @Column(name = "cliente_id", nullable = false)
    private UUID clienteId;

    @Column(nullable = false, unique = true, length = 10)
    private String placa;

    @Column(nullable = false, length = 100)
    private String marca;

    @Column(nullable = false, length = 100)
    private String modelo;

    @Column(nullable = false)
    private int ano;

    @Column(name = "deletado_em")
    private LocalDateTime deletadoEm;
}
