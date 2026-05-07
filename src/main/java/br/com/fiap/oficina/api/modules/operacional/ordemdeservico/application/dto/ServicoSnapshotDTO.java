package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto;

import java.math.BigDecimal;
import java.util.UUID;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;

public record ServicoSnapshotDTO(
        UUID id,
        String nome,
        BigDecimal precoBase,
        TipoServico tipo
) {}






