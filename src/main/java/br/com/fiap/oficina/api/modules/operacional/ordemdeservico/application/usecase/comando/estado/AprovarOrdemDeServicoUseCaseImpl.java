package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.estado;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.api.dto.AprovarServicoRequest;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoStatus;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicoStatus;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event.OrdemServicoAprovada;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event.OrdemServicoRejeitada;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway.CatalogoProdutoGateway;
import io.vertx.core.eventbus.EventBus;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class AprovarOrdemDeServicoUseCaseImpl implements AprovarOrdemDeServicoUseCase {

    private final OrdemDeServicoRepository repository;
    private final CatalogoProdutoGateway produtoGateway;
    private final EventBus eventBus;

    @Override
    public void executar(UUID id, AprovarServicoRequest request) {
        OrdemDeServico ordem = repository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Ordem de serviço não encontrada"));

        if (ordem.getStatus() != OrdemDeServicoStatus.AGUARDANDO_APROVACAO &&
                ordem.getStatus() != OrdemDeServicoStatus.APROVADA &&
                ordem.getStatus() != OrdemDeServicoStatus.EM_EXECUCAO) {
            throw new IllegalArgumentException(
                    "Apenas ordens AGUARDANDO_APROVACAO, APROVADA ou EM_EXECUCAO podem ser aprovadas.");
        }

        // Valida se algum serviço corretivo está sendo rejeitado
        if (request.servicosRejeitados() != null) {
            for (UUID idInstancia : request.servicosRejeitados()) {
                ordem.getServicos().stream()
                        .filter(s -> s.getId().equals(idInstancia))
                        .findFirst()
                        .ifPresent(s -> {
                            if (s.getTipo() == TipoServico.CORRETIVO) {
                                throw new IllegalArgumentException(
                                        "Serviço corretivo '" + s.getNome() + "' não pode ser rejeitado.");
                            }
                        });
            }
        }

        // Atualiza status dos serviços aprovados
        if (request.servicosAprovados() != null) {
            ordem.getServicos().stream()
                    .filter(s -> request.servicosAprovados().contains(s.getId()))
                    .forEach(s -> s.setStatus(OrdemDeServicoServicoStatus.APROVADO));
        }

        // Atualiza status dos serviços rejeitados e libera estoque
        if (request.servicosRejeitados() != null) {
            ordem.getServicos().stream()
                    .filter(s -> request.servicosRejeitados().contains(s.getId()))
                    .forEach(s -> {
                        s.setStatus(OrdemDeServicoServicoStatus.CANCELADO);
                        // Libera o estoque de todos os produtos associados a este serviço
                        if (s.getProdutos() != null) {
                            s.getProdutos().forEach(p -> 
                                produtoGateway.liberarEstoqueReservado(p.getProdutoId(), p.getQuantidade())
                            );
                        }
                    });
        }

        // Decide status final da OS
        boolean temAprovado = ordem.getServicos().stream()
                .anyMatch(s -> s.getStatus() == OrdemDeServicoServicoStatus.APROVADO);

        if (temAprovado) {
            // Se já estava EM_EXECUCAO, mantém. Se não, vai para APROVADA.
            if (ordem.getStatus() != OrdemDeServicoStatus.EM_EXECUCAO) {
                ordem.setStatus(OrdemDeServicoStatus.APROVADA);
            }
            repository.atualizar(ordem);

            OrdemServicoAprovada event = new OrdemServicoAprovada(ordem.getId());
            eventBus.publish(OrdemServicoAprovada.TOPICO, event.toJson());
        } else {
            // Se já estava APROVADA ou EM_EXECUCAO, não muda para REJEITADA (pois há itens
            // aprovados anteriormente)
            if (ordem.getStatus() == OrdemDeServicoStatus.AGUARDANDO_APROVACAO) {
                ordem.setStatus(OrdemDeServicoStatus.REJEITADA);
                repository.atualizar(ordem);

                OrdemServicoRejeitada event = new OrdemServicoRejeitada(ordem.getId());
                eventBus.publish(OrdemServicoRejeitada.TOPICO, event.toJson());
            } else {
                // Apenas salva as alterações nos itens (rejeições de novos itens)
                repository.atualizar(ordem);
            }
        }
    }
}






