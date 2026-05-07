package br.com.fiap.oficina.api.modules.relatorios.api.dto;

import java.util.UUID;

public record RelatorioTempoMedioServicoResponse(
    UUID servicoId,
    String nomeServico,
    Integer quantidadeExecucoesHistoricas,
    Long tempoMedioMinutos
) {}






