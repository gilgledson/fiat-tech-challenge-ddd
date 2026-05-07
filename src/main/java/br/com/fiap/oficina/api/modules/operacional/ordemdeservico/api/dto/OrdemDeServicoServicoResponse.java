package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrdemDeServicoServicoResponse(
        UUID id,
        @JsonProperty("servico_id") UUID servicoId,
        @Schema(description = "Nome do serviço executado") String nome,
        @Schema(description = "Quantidade executada") int quantidade,
        @JsonProperty("valor_unitario") @Schema(description = "Valor unitário cobrado") BigDecimal valorUnitario,
        @JsonProperty("valor_total") @Schema(description = "Valor total do serviço (qtd x unitário)") BigDecimal valorTotal,
        @Schema(description = "Status de execução do serviço") String status,
        @Schema(description = "Tipo de serviço") TipoServico tipo,
        @JsonProperty("data_inicio_execucao") java.time.LocalDateTime dataInicioExecucao,
        @JsonProperty("data_fim_execucao") java.time.LocalDateTime dataFimExecucao,
        @JsonProperty("usuario_executor_id") UUID usuarioExecutorId,
        String duracao,
        List<OrdemDeServicoProdutoResponse> produtos
) {}






