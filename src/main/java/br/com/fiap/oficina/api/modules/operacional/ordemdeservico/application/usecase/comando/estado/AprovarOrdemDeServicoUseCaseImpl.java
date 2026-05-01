package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.estado;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.api.dto.AprovarServicoRequest;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoStatus;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicoStatus;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event.OrdemServicoAprovada;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event.OrdemServicoRejeitada;
import io.vertx.core.eventbus.EventBus;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class AprovarOrdemDeServicoUseCaseImpl implements AprovarOrdemDeServicoUseCase {

    private final OrdemDeServicoRepository repository;
    private final EventBus eventBus;

    @Override
    public void executar(UUID id, AprovarServicoRequest request) {
        OrdemDeServico ordem = repository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Ordem de serviço não encontrada"));

        if (ordem.getStatus() != OrdemDeServicoStatus.AGUARDANDO_APROVACAO) {
            throw new IllegalArgumentException("Apenas ordens AGUARDANDO_APROVACAO podem ser aprovadas.");
        }

        // Valida se algum serviço corretivo está sendo rejeitado
        if (request.servicosRejeitados() != null) {
            for (UUID servicoId : request.servicosRejeitados()) {
                ordem.getServicos().stream()
                    .filter(s -> s.getServicoId().equals(servicoId))
                    .findFirst()
                    .ifPresent(s -> {
                        if (s.getTipo() == TipoServico.CORRETIVO) {
                            throw new IllegalArgumentException("Serviço corretivo '" + s.getNome() + "' não pode ser rejeitado.");
                        }
                    });
            }
        }

        // Atualiza status dos serviços aprovados
        if (request.servicosAprovados() != null) {
            ordem.getServicos().stream()
                .filter(s -> request.servicosAprovados().contains(s.getServicoId()))
                .forEach(s -> s.setStatus(OrdemDeServicoServicoStatus.APROVADO));
        }

        // Atualiza status dos serviços rejeitados
        if (request.servicosRejeitados() != null) {
            ordem.getServicos().stream()
                .filter(s -> request.servicosRejeitados().contains(s.getServicoId()))
                .forEach(s -> s.setStatus(OrdemDeServicoServicoStatus.REJEITADO));
        }

        // Decide status final da OS
        boolean temAprovado = ordem.getServicos().stream()
            .anyMatch(s -> s.getStatus() == OrdemDeServicoServicoStatus.APROVADO);

        if (temAprovado) {
            ordem.setStatus(OrdemDeServicoStatus.APROVADA);
            repository.atualizar(ordem);
            
            OrdemServicoAprovada event = new OrdemServicoAprovada(ordem.getId());
            eventBus.publish(OrdemServicoAprovada.TOPICO, event.toJson());
        } else {
            ordem.setStatus(OrdemDeServicoStatus.REJEITADA);
            repository.atualizar(ordem);

            OrdemServicoRejeitada event = new OrdemServicoRejeitada(ordem.getId());
            eventBus.publish(OrdemServicoRejeitada.TOPICO, event.toJson());
        }
    }
}
