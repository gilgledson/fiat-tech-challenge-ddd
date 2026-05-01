package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdemDeServico {
    private UUID id;
    private UUID clienteId;
    private UUID veiculoId;
    private String descricaoProblema;
    private String descricaoServico;
    private String descricaoManutencao;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataInicioExecucao;
    private LocalDateTime dataFimExecucao;
    private String motivoCancelamento;
    private OrdemDeServicoStatus status;
    private List<OrdemDeServicoProdutos> produtos;
    private List<OrdemDeServicoServicos> servicos;
    private LocalDateTime deletadoEm;

    public java.math.BigDecimal calcularValorTotal() {
        java.math.BigDecimal totalProdutos = produtos.stream()
                .map(OrdemDeServicoProdutos::getTotal)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        java.math.BigDecimal totalServicos = servicos.stream()
                .map(OrdemDeServicoServicos::getTotal)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        return totalProdutos.add(totalServicos);
    }
}
