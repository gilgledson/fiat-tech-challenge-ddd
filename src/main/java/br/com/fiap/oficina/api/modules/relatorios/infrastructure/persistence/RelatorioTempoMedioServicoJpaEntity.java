package br.com.fiap.oficina.api.modules.relatorios.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

import com.google.errorprone.annotations.Immutable;

@Entity
@Table(name = "vw_relatorio_tempo_medio_servico")
@Immutable // 🛡️ Apenas leitura!
@Getter
public class RelatorioTempoMedioServicoJpaEntity {

    @Id
    @Column(name = "servico_id")
    private UUID servicoId;

    @Column(name = "nome_servico")
    private String nomeServico;

    @Column(name = "quantidade_execucoes_historicas")
    private Integer quantidadeExecucoesHistoricas;

    @Column(name = "tempo_medio_minutos")
    private Long tempoMedioMinutos;

}






