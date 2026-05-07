package br.com.fiap.oficina.api.modules.relatorios.domain;

import java.util.UUID;

public record RelatorioTempoMedioServico(
    UUID servicoId,
    String nomeServico,
    Integer quantidadeExecucoesHistoricas,
    Long tempoMedioMinutos
) {}






