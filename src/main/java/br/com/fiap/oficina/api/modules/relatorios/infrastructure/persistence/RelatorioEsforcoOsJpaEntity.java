package br.com.fiap.oficina.api.modules.relatorios.infrastructure.persistence;

import java.util.UUID;

import com.google.errorprone.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "vw_relatorio_esforco_os")
@Immutable
@Getter
public class RelatorioEsforcoOsJpaEntity {

    @Id
    @Column(name = "ordem_de_servico_id")
    private UUID ordemDeServicoId;

    @Column(name = "total_servicos_realizados")
    private Integer totalServicosRealizados;

    @Column(name = "esforco_total_minutos")
    private Long esforcoTotalMinutos;
}






