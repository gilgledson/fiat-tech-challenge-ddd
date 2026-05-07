package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProdutoSnapshotDTO(
        UUID id,
        String nome,
        BigDecimal precoUnitario
) {}






