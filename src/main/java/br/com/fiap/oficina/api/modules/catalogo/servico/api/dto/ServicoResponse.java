package br.com.fiap.oficina.api.modules.catalogo.servico.api.dto;

import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.dto.ServicoOutput;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ServicoResponse(
        UUID id,
        String nome,
        TipoServico tipo,
        @JsonProperty("preco_base") BigDecimal precoBase,
        @JsonProperty("produtos_sugeridos") List<ProdutoSugeridoResponse> produtosSugeridos,
        @JsonProperty("deletado_em") LocalDateTime deletadoEm) {

    public static ServicoResponse fromOutput(ServicoOutput servico) {

        List<ProdutoSugeridoResponse> listaProdutosDto = List.of();

        if (servico.produtosSugeridos() != null) {
            listaProdutosDto = servico.produtosSugeridos().stream()
                    .map(ProdutoSugeridoResponse::fromOutput)
                    .toList();
        }

        return new ServicoResponse(
                servico.id(),
                servico.nome(),
                servico.tipo(),
                servico.precoBase(),
                listaProdutosDto,
                servico.deletadoEm());
    }
}






