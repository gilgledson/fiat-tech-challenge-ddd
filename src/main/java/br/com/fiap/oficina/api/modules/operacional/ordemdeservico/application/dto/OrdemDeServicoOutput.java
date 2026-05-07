package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicos;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrdemDeServicoOutput(
        UUID id,
        UUID clienteId,
        UUID veiculoId,
        String descricaoProblema,
        OrdemDeServicoStatus status,
        LocalDateTime dataAbertura,
        LocalDateTime dataInicioExecucao,
        LocalDateTime dataFimExecucao,
        List<OrdemDeServicoServicos> servicos) {
}






