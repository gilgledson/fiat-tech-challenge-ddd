package br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase;

import br.com.fiap.oficina.api.modules.catalogo.servico.application.repository.ServicoRepository;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.dto.ServicoOutput;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.mapper.ServicoOutputMapper;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.validator.ProdutoSugeridoValidator;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.ProdutoSugerido;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.Servico;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;
import jakarta.ws.rs.NotFoundException;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class EditarServicoUseCaseImpl implements EditarServicoUseCase {
    private final ServicoRepository servicoRepository;
    private final ServicoOutputMapper servicoOutputMapper;
    private final ProdutoSugeridoValidator produtoSugeridoValidator;

    @Override
    public ServicoOutput executar(UUID id, String nome, TipoServico tipo, BigDecimal precoBase,
            List<ProdutoSugerido> produtosSugeridos) {

        Servico servico = servicoRepository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Servico " + id.toString() + " não encontrado ou desativado"));

        produtoSugeridoValidator.validar(produtosSugeridos);

        servico.atualizar(nome, tipo, precoBase, produtosSugeridos);

        servicoRepository.atualizar(servico);

        return this.servicoOutputMapper.mapear(servico);

    }
}






