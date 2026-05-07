package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.item;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.OrdemDeServicoOutput;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.mapper.OrdemDeServicoOutputMapper;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway.CatalogoProdutoGateway;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicos;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class RemoverServicoOrdemDeServicoUseCaseImpl implements RemoverServicoOrdemDeServicoUseCase {

    private final OrdemDeServicoRepository osRepository;
    private final CatalogoProdutoGateway produtoGateway;

    @Override
    public OrdemDeServicoOutput executar(UUID ordemDeServicoId, UUID id) {
        OrdemDeServico ordem = osRepository.buscarPorId(ordemDeServicoId)
                .orElseThrow(() -> new NotFoundException("Ordem de serviço não encontrada"));

        if (ordem.getStatus().isEncerrada()) {
            throw new IllegalArgumentException("Não é possível remover serviços de uma ordem de serviço encerrada.");
        }

        OrdemDeServicoServicos itemARemover = ordem.getServicos().stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Serviço não encontrado nesta ordem de serviço"));

        // Libera o estoque de todos os produtos associados a este serviço
        itemARemover.getProdutos().forEach(produto -> {
            produtoGateway.liberarEstoqueReservado(produto.getProdutoId(), produto.getQuantidade());
        });

        ordem.getServicos().remove(itemARemover);
        osRepository.atualizar(ordem);

        return OrdemDeServicoOutputMapper.toOutput(ordem);
    }
}






