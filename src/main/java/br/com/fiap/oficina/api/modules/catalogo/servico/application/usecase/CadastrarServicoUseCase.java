package br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase;

import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.dto.ServicoOutput;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.ProdutoSugerido;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;

import java.math.BigDecimal;
import java.util.List;

public interface CadastrarServicoUseCase {

    public ServicoOutput execute(String nome, TipoServico tipo, BigDecimal precoBase,
            List<ProdutoSugerido> produtosSugeridos);

}






