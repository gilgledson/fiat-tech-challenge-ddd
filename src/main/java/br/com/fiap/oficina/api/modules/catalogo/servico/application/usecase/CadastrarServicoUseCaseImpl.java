package br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase;

import br.com.fiap.oficina.api.modules.catalogo.servico.application.repository.ServicoRepository;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.dto.ServicoOutput;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.mapper.ServicoOutputMapper;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.validator.ProdutoSugeridoValidator;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.ProdutoSugerido;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.Servico;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class CadastrarServicoUseCaseImpl implements CadastrarServicoUseCase {
    private final ServicoRepository servicoRepository;
    private final ProdutoSugeridoValidator produtoSugeridoValidator;
    private final ServicoOutputMapper servicoOutputMapper;

    @Override
    public ServicoOutput execute(String nome, TipoServico tipo, BigDecimal precoBase,
            List<ProdutoSugerido> produtosSugeridos) {

        produtoSugeridoValidator.validar(produtosSugeridos);

        Servico novoServico = new Servico(nome, tipo, precoBase, produtosSugeridos);
        servicoRepository.salvar(novoServico);

        return servicoOutputMapper.mapear(novoServico);
    }
}






