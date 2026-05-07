package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.mapper;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.OrdemDeServicoOutput;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;

public class OrdemDeServicoOutputMapper {

    private OrdemDeServicoOutputMapper() {
    }

    public static OrdemDeServicoOutput toOutput(OrdemDeServico ordem) {
        return new OrdemDeServicoOutput(
                ordem.getId(),
                ordem.getClienteId(),
                ordem.getVeiculoId(),
                ordem.getDescricaoProblema(),
                ordem.getStatus(),
                ordem.getDataAbertura(),
                ordem.getDataInicioExecucao(),
                ordem.getDataFimExecucao(),
                ordem.getServicos());
    }
}






