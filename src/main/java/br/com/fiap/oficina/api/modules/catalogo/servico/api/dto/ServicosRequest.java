package br.com.fiap.oficina.api.modules.catalogo.servico.api.dto;

import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.ProdutoSugerido;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public record ServicosRequest(
        @NotBlank(message = "A descrição do serviço é obrigatória") @Schema(description = "Nome do serviço", defaultValue = "Troca de Óleo e Filtro") String nome,

        @NotNull(message = "O tipo do serviço é obrigatório") @Schema(description = "Categoria do serviço", defaultValue = "PREVENTIVO") TipoServico tipo,

        @NotNull(message = "O preço base é obrigatório") @DecimalMin(value = "0.01", message = "O preço base da mão de obra deve ser maior que zero") @Schema(description = "Valor cobrado pela mão de obra", defaultValue = "150.00") @JsonProperty("preco_base") BigDecimal precoBase,

        @Schema(description = "Lista opcional de insumos/produtos necessários para este serviço") @JsonProperty("produtos_sugeridos") List<@Valid ProdutoSugeridoRequest> produtosSugeridos) {
    public List<ProdutoSugerido> mapearProdutosParaDominio() {
        if (this.produtosSugeridos == null || this.produtosSugeridos.isEmpty()) {
            return new ArrayList<>();
        }
        return this.produtosSugeridos.stream()
                .map(req -> new ProdutoSugerido(req.produtoId(), req.quantidade()))
                .toList();
    }
}






