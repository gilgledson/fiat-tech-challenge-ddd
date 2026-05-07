package br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.validator;

import br.com.fiap.oficina.api.modules.catalogo.produto.application.repository.ProdutoRepository;
import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.Produto;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.ProdutoSugerido;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ProdutoSugeridoValidator {

    private final ProdutoRepository produtoRepository;

    public void validar(List<ProdutoSugerido> produtosSugeridos) {
        if (produtosSugeridos == null || produtosSugeridos.isEmpty()) {
            return;
        }

        Set<UUID> idsParaBuscar = produtosSugeridos.stream()
                .map(p -> p.produtoId())
                .collect(Collectors.toSet());

        List<Produto> produtosEncontrados = produtoRepository.buscarPorIds(idsParaBuscar.stream().toList());

        Map<UUID, Produto> mapaProdutos = produtosEncontrados.stream()
                .collect(Collectors.toMap(Produto::getId, p -> p));

        for (ProdutoSugerido item : produtosSugeridos) {
            Produto produto = mapaProdutos.get(item.produtoId());

            if (produto == null) {
                throw new IllegalArgumentException(
                        "Produto com ID " + item.produtoId() + " não encontrado no catálogo.");
            }

            if (produto.getDeletadoEm().isPresent()) {
                throw new IllegalArgumentException(
                        "Não é possível associar o produto '" + produto.getNome() + "' pois ele está inativo.");
            }
        }
    }
}






