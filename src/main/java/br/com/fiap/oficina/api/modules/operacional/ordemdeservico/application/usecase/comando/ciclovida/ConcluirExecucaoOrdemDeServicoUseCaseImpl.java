package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.ciclovida;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event.OrdemServicoExecucaoConcluidaEvent;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway.CatalogoProdutoGateway;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicoStatus;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoStatus;
import io.vertx.core.eventbus.EventBus;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class ConcluirExecucaoOrdemDeServicoUseCaseImpl implements ConcluirExecucaoOrdemDeServicoUseCase {

    private final OrdemDeServicoRepository repository;
    private final CatalogoProdutoGateway produtoGateway;
    private final EventBus eventBus;

    @Override
    @Transactional
    public void executar(UUID id) {
        OrdemDeServico ordem = repository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Ordem de serviço não encontrada"));

        if (ordem.getStatus() != OrdemDeServicoStatus.EM_EXECUCAO) {
            throw new IllegalArgumentException("Apenas ordens EM_EXECUCAO podem ser finalizadas.");
        }

        // Valida se todos os serviços (que não foram rejeitados) foram concluídos
        boolean temServicoPendente = ordem.getServicos().stream()
                .filter(s -> s.getStatus() != OrdemDeServicoServicoStatus.REJEITADO)
                .anyMatch(s -> s.getStatus() != OrdemDeServicoServicoStatus.FINALIZADO);

        if (temServicoPendente) {
            throw new IllegalArgumentException(
                    "A ordem não pode ser finalizada enquanto houver serviços pendentes ou em execução.");
        }

        // Confirma a venda de todos os produtos reservados em cada serviço
        ordem.getServicos().forEach(servico -> {
            servico.getProdutos().forEach(item -> {
                produtoGateway.confirmarVenda(item.getProdutoId(), item.getQuantidade());
            });
        });

        ordem.setStatus(OrdemDeServicoStatus.AGUARDANDO_PAGAMENTO);
        ordem.setDataFimExecucao(LocalDateTime.now());

        repository.atualizar(ordem);

        OrdemServicoExecucaoConcluidaEvent event = new OrdemServicoExecucaoConcluidaEvent(
                ordem.getId(),
                ordem.calcularValorTotal(),
                ordem.getClienteId());

        System.out.println("📢 [Operacional] Publicando evento no barramento: OS " + ordem.getId() + " finalizada.");

        eventBus.publish(OrdemServicoExecucaoConcluidaEvent.TOPICO, event.toJson());
    }
}






