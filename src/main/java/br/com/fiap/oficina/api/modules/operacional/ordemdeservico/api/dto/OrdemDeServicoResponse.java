package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.api.dto;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.OrdemDeServicoOutput;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoProdutos;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicos;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrdemDeServicoResponse(
        UUID id,
        @JsonProperty("cliente_id") UUID clienteId,
        @JsonProperty("veiculo_id") UUID veiculoId,
        @JsonProperty("descricao_problema") String descricaoProblema,
        OrdemDeServicoStatus status,
        @JsonProperty("data_abertura") LocalDateTime dataAbertura,
        @JsonProperty("data_inicio_execucao") LocalDateTime dataInicioExecucao,
        @JsonProperty("data_fim_execucao") LocalDateTime dataFimExecucao,
        List<OrdemDeServicoProdutoResponse> produtos,
        List<OrdemDeServicoServicoResponse> servicos) {

    public static OrdemDeServicoResponse fromOutput(OrdemDeServicoOutput output) {

        List<OrdemDeServicoProdutoResponse> produtosDto = List.of();
        if (output.produtos() != null) {
            produtosDto = output.produtos().stream()
                    .map(OrdemDeServicoResponse::mapProduto)
                    .toList();
        }

        List<OrdemDeServicoServicoResponse> servicosDto = List.of();
        if (output.servicos() != null) {
            servicosDto = output.servicos().stream()
                    .map(OrdemDeServicoResponse::mapServico)
                    .toList();
        }

        return new OrdemDeServicoResponse(
                output.id(),
                output.clienteId(),
                output.veiculoId(),
                output.descricaoProblema(),
                output.status(),
                output.dataAbertura(),
                output.dataInicioExecucao(),
                output.dataFimExecucao(),
                produtosDto,
                servicosDto);
    }

    private static OrdemDeServicoProdutoResponse mapProduto(OrdemDeServicoProdutos p) {
        return new OrdemDeServicoProdutoResponse(
                p.getProdutoId(),
                p.getNomeDoProduto(),
                p.getQuantidade(),
                p.getPrecoUnitario(),
                p.getTotal());
    }

    private static OrdemDeServicoServicoResponse mapServico(OrdemDeServicoServicos s) {
        return new OrdemDeServicoServicoResponse(
                s.getServicoId(),
                s.getNome(),
                s.getQuantidade(),
                s.getPrecoUnitario(),
                s.getTotal(),
                s.getStatus().name(),
                s.getDataInicioExecucao(),
                s.getDataFimExecucao(),
                s.getUsuarioExecutorId(),
                s.getDuracao());
    }
}
