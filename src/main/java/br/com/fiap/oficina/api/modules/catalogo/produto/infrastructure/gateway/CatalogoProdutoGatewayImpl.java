package br.com.fiap.oficina.api.modules.catalogo.produto.infrastructure.gateway;

import br.com.fiap.oficina.api.modules.catalogo.produto.application.repository.ProdutoRepository;
import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.Produto;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.ProdutoSnapshotDTO;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.gateway.CatalogoProdutoGateway;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CatalogoProdutoGatewayImpl implements CatalogoProdutoGateway {

    @Inject
    ProdutoRepository produtoRepository;

    @Override
    public Optional<ProdutoSnapshotDTO> buscarPorId(UUID id) {
        return produtoRepository.buscarPorId(id)
                .map(p -> new ProdutoSnapshotDTO(p.getId(), p.getNome(), p.getPrecoUnitario()));
    }

    @Override
    @Transactional
    public void reservarEstoque(UUID id, BigDecimal quantidade) {
        Produto produto = buscarOuFalhar(id);
        produto.reservarEstoque(quantidade);
        produtoRepository.editar(produto);
    }

    @Override
    @Transactional
    public void liberarEstoqueReservado(UUID id, BigDecimal quantidade) {
        Produto produto = buscarOuFalhar(id);
        produto.liberarEstoqueReservado(quantidade);
        produtoRepository.editar(produto);
    }

    @Override
    @Transactional
    public void confirmarVenda(UUID id, BigDecimal quantidade) {
        Produto produto = buscarOuFalhar(id);
        produto.confirmarVendaDeReserva(quantidade);
        produtoRepository.editar(produto);
    }

    private Produto buscarOuFalhar(UUID id) {
        return produtoRepository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + id));
    }
}






