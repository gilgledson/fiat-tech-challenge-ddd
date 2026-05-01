package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.infrastructure.seeder;

import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.Produto;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.Servico;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.*;
import jakarta.enterprise.context.ApplicationScoped;
import net.datafaker.Faker;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class OrdemServicoFactory {

    private static final Faker faker = new Faker();

    public OrdemDeServico create(UUID clienteId, UUID veiculoId, List<Servico> servicosDisponiveis, List<Produto> produtosDisponiveis) {
        OrdemDeServico os = new OrdemDeServico();
        os.setId(UUID.randomUUID());
        os.setClienteId(clienteId);
        os.setVeiculoId(veiculoId);
        os.setDescricaoProblema(faker.lorem().sentence());
        os.setDataAbertura(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30)));
        os.setStatus(OrdemDeServicoStatus.ENTREGUE); // Por padrao finalizada para relatorios
        
        List<OrdemDeServicoServicos> osServicos = new ArrayList<>();
        int numServicos = faker.number().numberBetween(1, 3);
        for (int i = 0; i < numServicos; i++) {
            Servico s = servicosDisponiveis.get(faker.number().numberBetween(0, servicosDisponiveis.size()));
            OrdemDeServicoServicos oss = new OrdemDeServicoServicos(
                os.getId(),
                s.getId(),
                s.getNome(),
                1,
                s.getPrecoBase(),
                s.getPrecoBase(),
                OrdemDeServicoServicoStatus.FINALIZADO,
                TipoServico.PREVENTIVO
            );
            oss.setDataInicioExecucao(os.getDataAbertura().plusHours(1));
            oss.setDataFimExecucao(oss.getDataInicioExecucao().plusHours(2));
            osServicos.add(oss);
        }
        os.setServicos(osServicos);

        List<OrdemDeServicoProdutos> osProdutos = new ArrayList<>();
        int numProdutos = faker.number().numberBetween(0, 4);
        for (int i = 0; i < numProdutos; i++) {
            Produto p = produtosDisponiveis.get(faker.number().numberBetween(0, produtosDisponiveis.size()));
            OrdemDeServicoProdutos osp = new OrdemDeServicoProdutos();
            osp.setOrdemDeServicoId(os.getId());
            osp.setProdutoId(p.getId());
            osp.setNomeDoProduto(p.getNome());
            osp.setQuantidade(BigDecimal.valueOf(faker.number().numberBetween(1, 3)));
            osp.setPrecoUnitario(p.getPrecoUnitario());
            osp.setTotal(osp.getPrecoUnitario().multiply(osp.getQuantidade()));
            osProdutos.add(osp);
        }
        os.setProdutos(osProdutos);

        os.setDataInicioExecucao(os.getDataAbertura().plusHours(1));
        os.setDataFimExecucao(os.getDataInicioExecucao().plusHours(5));

        return os;
    }
}
