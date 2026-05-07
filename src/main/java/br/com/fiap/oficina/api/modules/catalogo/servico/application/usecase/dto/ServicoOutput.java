package br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.dto;

import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ServicoOutput(
                UUID id,
                String nome,
                TipoServico tipo,
                BigDecimal precoBase,
                List<ProdutoSugeridoOutput> produtosSugeridos,
                LocalDateTime deletadoEm) {
}






