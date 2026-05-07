package br.com.fiap.oficina.api.modules.catalogo.produto.api.dto;

import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.Produto;
import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.UnidadeMedida;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public record ProdutoResponse(UUID id,
        String nome,
        @JsonProperty("codigo_barras") String codigoBarras,
        @JsonProperty("preco_unitario") BigDecimal precoUnitario,
        @JsonProperty("quantidade_estoque_fisico") BigDecimal quantidadeEstoqueFisico,
        @JsonProperty("quantidade_estoque_reservado") BigDecimal quantidadeEstoqueReservado,
        @JsonProperty("unidade_medida") UnidadeMedida unidadeMedida,
        @JsonProperty("deletado_em") Optional<LocalDateTime> deletadoEm) {

    public static ProdutoResponse fromEntity(Produto produto) {
        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getCodigoBarras(),
                produto.getPrecoUnitario(),
                produto.getQuantidadeEstoqueFisico(),
                produto.getQuantidadeEstoqueReservado(),
                produto.getUnidadeMedida(),
                produto.getDeletadoEm());

    }
}






