package br.com.fiap.oficina.api.modules.catalogo.produto.api.dto;

import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.UnidadeMedida;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

public record ProdutoRequest(
                @NotBlank(message = "O nome é obrigatório") @Schema(description = "nome do produto", defaultValue = "Parafuso sextavado 10x50") String nome,
                @NotBlank(message = "O código de barras é obrigatório") @JsonProperty("codigo_barras") @Schema(description = "código de barras do produto", defaultValue = "7891195709488") String codigoBarras,
                @NotNull(message = "O preço unitário é obrigatório") @JsonProperty("preco_unitario") @Schema(description = "preço unitario do produto", defaultValue = "0.50") BigDecimal precoUnitario,
                @NotNull(message = "A quantidade em estoque físico é obrigatória") @JsonProperty("quantidade_estoque_fisico") @Schema(description = "quantidade em estoque físico do produto", defaultValue = "10") BigDecimal quantidadeEstoqueFisico,
                @NotNull(message = "A unidade de medida é obrigatória") @JsonProperty("unidade_medida") @Schema(description = "unidade de medida do produto", defaultValue = "UN") UnidadeMedida unidadeMedida) {

}






