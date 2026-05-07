package br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.mapper;

import br.com.fiap.oficina.api.modules.catalogo.produto.application.repository.ProdutoRepository;
import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.Produto;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.dto.ProdutoSugeridoOutput;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.dto.ServicoOutput;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.Servico;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ServicoOutputMapper {

        private final ProdutoRepository produtoRepository;

        public List<ServicoOutput> mapearLista(List<Servico> servicos) {
                if (servicos == null || servicos.isEmpty()) {
                        return List.of();
                }

                Set<UUID> todosIds = servicos.stream()
                                .flatMap(s -> s.getProdutosSugeridos().stream())
                                .map(p -> p.produtoId())
                                .collect(Collectors.toSet());

                Map<UUID, String> mapaNomes = buscarMapaDeNomes(todosIds);

                return servicos.stream()
                                .map(servico -> montarOutput(servico, mapaNomes))
                                .toList();
        }

        public ServicoOutput mapear(Servico servico) {
                Set<UUID> ids = servico.getProdutosSugeridos().stream()
                                .map(p -> p.produtoId())
                                .collect(Collectors.toSet());

                Map<UUID, String> mapaNomes = buscarMapaDeNomes(ids);

                return montarOutput(servico, mapaNomes);
        }

        private Map<UUID, String> buscarMapaDeNomes(Set<UUID> ids) {
                if (ids.isEmpty())
                        return Map.of();

                List<Produto> produtos = produtoRepository.buscarPorIds(ids.stream().toList());
                return produtos.stream()
                                .collect(Collectors.toMap(Produto::getId, Produto::getNome));
        }

        private ServicoOutput montarOutput(Servico servico, Map<UUID, String> mapaNomes) {
                List<ProdutoSugeridoOutput> insumos = servico.getProdutosSugeridos().stream()
                                .map(insumo -> new ProdutoSugeridoOutput(
                                                insumo.produtoId(),
                                                mapaNomes.getOrDefault(insumo.produtoId(),
                                                                "Produto Indisponível/Excluído"),
                                                insumo.quantidade()))
                                .toList();

                return new ServicoOutput(
                                servico.getId(),
                                servico.getNome(),
                                servico.getTipo(),
                                servico.getPrecoBase(),
                                insumos,
                                servico.getDeletadoEm());
        }
}






