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
    public void executar(UUID ordemId, UUID servicoId, UUID usuarioExecutorId) {
        OrdemDeServico ordem = repository.buscarPorId(ordemId)
                .orElseThrow(() -> new NotFoundException("Ordem de serviço não encontrada"));

        if (ordem.getStatus() != OrdemDeServicoStatus.EM_EXECUCAO) {
            throw new IllegalArgumentException("Apenas serviços de ordens EM_EXECUCAO podem ser iniciados.");
        }

        var servico = ordem.getServicos().stream()
                .filter(s -> s.getServicoId().equals(servicoId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Serviço não encontrado nesta ordem"));

        servico.setStatus(OrdemDeServicoServicoStatus.EM_EXECUCAO);
        servico.setDataInicioExecucao(LocalDateTime.now());
        servico.setUsuarioExecutorId(usuarioExecutorId);

        repository.atualizar(ordem);
    }
}
