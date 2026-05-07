package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.item;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicoStatus;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoStatus;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class FinalizarExecucaoServicoUseCaseImpl implements FinalizarExecucaoServicoUseCase {

    private final OrdemDeServicoRepository repository;

    @Override
    public void executar(UUID ordemId, UUID id) {
        OrdemDeServico ordem = repository.buscarPorId(ordemId)
                .orElseThrow(() -> new NotFoundException("Ordem de serviço não encontrada"));

        if (ordem.getStatus() != OrdemDeServicoStatus.EM_EXECUCAO) {
            throw new IllegalArgumentException("Apenas serviços de ordens EM_EXECUCAO podem ser finalizados.");
        }

        var servico = ordem.getServicos().stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Serviço não encontrado nesta ordem"));

        if (servico.getStatus() != OrdemDeServicoServicoStatus.EM_EXECUCAO) {
            throw new IllegalArgumentException("O serviço deve estar EM_EXECUCAO para ser finalizado.");
        }

        servico.setStatus(OrdemDeServicoServicoStatus.FINALIZADO);
        servico.setDataFimExecucao(LocalDateTime.now());

        repository.atualizar(ordem);
    }
}






