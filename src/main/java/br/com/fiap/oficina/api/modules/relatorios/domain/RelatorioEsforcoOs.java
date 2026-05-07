package br.com.fiap.oficina.api.modules.relatorios.domain;

import java.util.UUID;

public record RelatorioEsforcoOs(
    UUID ordemDeServicoId,
    Integer totalServicosRealizados,
    Long esforcoTotalMinutos
) {}






