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
public class IniciarExecucaoServicoUseCaseImpl implements IniciarExecucaoServicoUseCase {

    private final OrdemDeServicoRepository repository;

    @Override
    public void executar(UUID ordemId, UUID id, UUID usuarioExecutorId) {
        OrdemDeServico ordem = repository.buscarPorId(ordemId)
                .orElseThrow(() -> new NotFoundException("Ordem de serviço não encontrada"));

        if (ordem.getStatus() != OrdemDeServicoStatus.APROVADA
                && ordem.getStatus() != OrdemDeServicoStatus.EM_EXECUCAO) {
            throw new IllegalArgumentException(
                    "Apenas serviços de ordens APROVADAS ou EM EXECUCAO podem ter serviços iniciados.");
        }

        var servico = ordem.getServicos().stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Serviço não encontrado nesta ordem"));

        if (servico.getStatus() != OrdemDeServicoServicoStatus.APROVADO) {
            throw new IllegalArgumentException("Apenas serviços com status APROVADO podem ser iniciados.");
        }

        servico.setStatus(OrdemDeServicoServicoStatus.EM_EXECUCAO);
        servico.setDataInicioExecucao(LocalDateTime.now());
        servico.setUsuarioExecutorId(usuarioExecutorId);

        repository.atualizar(ordem);
    }
}






