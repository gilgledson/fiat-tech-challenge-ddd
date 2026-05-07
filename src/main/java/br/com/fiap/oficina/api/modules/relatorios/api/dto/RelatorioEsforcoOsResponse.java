package br.com.fiap.oficina.api.modules.relatorios.api.dto;

import java.util.UUID;

public record RelatorioEsforcoOsResponse(
    UUID ordemDeServicoId,
    Integer totalServicosRealizados,
    Long esforcoTotalMinutos
) {}






