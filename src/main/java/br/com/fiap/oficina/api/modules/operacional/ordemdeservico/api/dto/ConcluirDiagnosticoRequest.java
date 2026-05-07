package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.api.dto;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicoStatus;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicos;

public record ConcluirDiagnosticoRequest(
        @Schema(description = "ID da ordem de serviço") UUID ordemDeServicoId,
        @Schema(description = "Lista de serviços a serem adicionados") List<@Valid ServicoOrcamentoRequest> servicos) {

    public List<OrdemDeServicoServicos> mapearServicosParaDominio() {
        return servicos.stream()
                .map(s -> new OrdemDeServicoServicos(
                        this.ordemDeServicoId,
                        s.servicoId(),
                        s.nome(),
                        s.quantidade(),
                        s.valorUnitario(),
                        s.calcularTotal(),
                        OrdemDeServicoServicoStatus.PENDENTE,
                        s.tipo()))
                .toList();
    }
}






