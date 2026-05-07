package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private List<OrdemDeServicoServicos> servicos = new ArrayList<>();
    private LocalDateTime deletadoEm;

    public java.math.BigDecimal calcularValorTotal() {
        return calcularValorTotal(false);
    }

    public java.math.BigDecimal calcularValorTotal(boolean incluirPendentes) {
        java.math.BigDecimal totalServicos = (servicos == null ? new ArrayList<OrdemDeServicoServicos>() : servicos).stream()
                .filter(s -> s.getStatus() == OrdemDeServicoServicoStatus.APROVADO || 
                             s.getStatus() == OrdemDeServicoServicoStatus.EM_EXECUCAO || 
                             s.getStatus() == OrdemDeServicoServicoStatus.FINALIZADO ||
                             (incluirPendentes && s.getStatus() == OrdemDeServicoServicoStatus.PENDENTE))
                .map(OrdemDeServicoServicos::getTotal)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
                
        return totalServicos;
    }
}






