package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.infrastructure.seeder;

import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.Produto;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.Servico;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;
import br.com.fiap.oficina.api.modules.operacional.funcionario.domain.entity.Funcionario;
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

    public OrdemDeServico create(UUID clienteId, UUID veiculoId, List<Servico> servicosDisponiveis, List<Produto> produtosDisponiveis, List<Funcionario> funcionarios, LocalDateTime dataAbertura) {
        OrdemDeServico os = new OrdemDeServico();
        os.setId(UUID.randomUUID());
        os.setClienteId(clienteId);
        os.setVeiculoId(veiculoId);
        os.setDescricaoProblema(faker.lorem().sentence());
        os.setDataAbertura(dataAbertura);
        
        // Randomiza o status da OS
        double statusProb = faker.number().randomDouble(2, 0, 1);
        if (statusProb < 0.7) {
            os.setStatus(OrdemDeServicoStatus.ENTREGUE);
        } else if (statusProb < 0.85) {
            os.setStatus(OrdemDeServicoStatus.CANCELADA);
        } else {
            os.setStatus(OrdemDeServicoStatus.EM_EXECUCAO);
        }
        
        List<OrdemDeServicoServicos> osServicos = new ArrayList<>();
        int numServicos = faker.number().numberBetween(1, 3);
        for (int i = 0; i < numServicos; i++) {
            Servico s = servicosDisponiveis.get(faker.number().numberBetween(0, servicosDisponiveis.size()));
            OrdemDeServicoServicoStatus servicoStatus = os.getStatus() == OrdemDeServicoStatus.ENTREGUE ? 
                OrdemDeServicoServicoStatus.FINALIZADO : 
                (os.getStatus() == OrdemDeServicoStatus.CANCELADA ? OrdemDeServicoServicoStatus.CANCELADO : OrdemDeServicoServicoStatus.EM_EXECUCAO);

            OrdemDeServicoServicos oss = new OrdemDeServicoServicos(
                os.getId(),
                s.getId(),
                s.getNome(),
                1,
                s.getPrecoBase(),
                s.getPrecoBase(),
                servicoStatus,
                TipoServico.PREVENTIVO
            );
            oss.setId(UUID.randomUUID());

            // Define executor aleatório se houver funcionários
            if (funcionarios != null && !funcionarios.isEmpty()) {
                Funcionario executor = funcionarios.get(faker.number().numberBetween(0, funcionarios.size()));
                oss.setUsuarioExecutorId(executor.getUsuarioId());
            }

            // Durações variadas (30 min a 6 horas)
            int duracaoMinutos = faker.number().numberBetween(30, 360);
            oss.setDataInicioExecucao(os.getDataAbertura().plusMinutes(faker.number().numberBetween(10, 60)));
            oss.setDataFimExecucao(oss.getDataInicioExecucao().plusMinutes(duracaoMinutos));
            
            // Adiciona produtos ao serviço
            int numProdutos = faker.number().numberBetween(0, 3);
            for (int j = 0; j < numProdutos; j++) {
                Produto p = produtosDisponiveis.get(faker.number().numberBetween(0, produtosDisponiveis.size()));
                OrdemDeServicoProdutos osp = new OrdemDeServicoProdutos();
                osp.setOrdemDeServicoId(os.getId());
                osp.setProdutoId(p.getId());
                osp.setNomeDoProduto(p.getNome());
                osp.setQuantidade(BigDecimal.valueOf(faker.number().numberBetween(1, 3)));
                osp.setPrecoUnitario(p.getPrecoUnitario());
                osp.setTotal(osp.getPrecoUnitario().multiply(osp.getQuantidade()));
                oss.getProdutos().add(osp);
            }
            oss.calcularTotal();
            osServicos.add(oss);
        }
        os.setServicos(osServicos);

        if (os.getStatus() == OrdemDeServicoStatus.ENTREGUE || os.getStatus() == OrdemDeServicoStatus.CANCELADA) {
            LocalDateTime maxFim = os.getServicos().stream()
                .filter(s -> s.getDataFimExecucao() != null)
                .map(OrdemDeServicoServicos::getDataFimExecucao)
                .max(LocalDateTime::compareTo)
                .orElse(os.getDataAbertura().plusHours(1));
            
            os.setDataInicioExecucao(os.getDataAbertura().plusMinutes(5));
            os.setDataFimExecucao(maxFim.plusMinutes(10));
        } else if (os.getStatus() == OrdemDeServicoStatus.EM_EXECUCAO) {
            os.setDataInicioExecucao(os.getDataAbertura().plusMinutes(5));
            os.setDataFimExecucao(null);
        }

        return os;
    }
}






