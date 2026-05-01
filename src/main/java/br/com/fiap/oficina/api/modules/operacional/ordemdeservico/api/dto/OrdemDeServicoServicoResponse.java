package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

public record OrdemDeServicoServicoResponse(
        @JsonProperty("servico_id") UUID servicoId,
        @Schema(description = "Nome do serviço executado") String nome,
        @Schema(description = "Quantidade executada") int quantidade,
        @JsonProperty("valor_unitario") @Schema(description = "Valor unitário cobrado") BigDecimal valorUnitario,
        @JsonProperty("valor_total") @Schema(description = "Valor total do serviço (qtd x unitário)") BigDecimal valorTotal,
        @Schema(description = "Status de execução do serviço") String status,
        @JsonProperty("data_inicio_execucao") java.time.LocalDateTime dataInicioExecucao,
        @JsonProperty("data_fim_execucao") java.time.LocalDateTime dataFimExecucao,
        @JsonProperty("usuario_executor_id") UUID usuarioExecutorId,
        String duracao
) {}
