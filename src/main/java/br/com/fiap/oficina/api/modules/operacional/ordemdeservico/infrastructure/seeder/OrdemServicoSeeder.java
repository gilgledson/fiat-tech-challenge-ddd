package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.infrastructure.seeder;

import br.com.fiap.oficina.api.modules.atendimento.cliente.application.repository.ClienteRepository;
import br.com.fiap.oficina.api.modules.atendimento.cliente.domain.entity.Cliente;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.repository.VeiculoRepository;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.domain.entity.Veiculo;
import br.com.fiap.oficina.api.modules.catalogo.produto.application.repository.ProdutoRepository;
import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.Produto;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.repository.ServicoRepository;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.Servico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.shared.infrastructure.seeder.Seeder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;

@ApplicationScoped
public class OrdemServicoSeeder implements Seeder {

    @ConfigProperty(name = "quarkus.profile")
    private String ambiente;

    @Inject
    OrdemDeServicoRepository repository;

    @Inject
    OrdemServicoFactory factory;

    @Inject
    ClienteRepository clienteRepository;

    @Inject
    VeiculoRepository veiculoRepository;

    @Inject
    ProdutoRepository produtoRepository;

    @Inject
    ServicoRepository servicoRepository;

    @Override
    public void execute() {
        if ("prod".equals(ambiente)) {
            return;
        }

        if (repository.buscarTodas(null, null, 0, 1, false).totalElementos() == 0) {
            List<Cliente> clientes = clienteRepository.listarTodos(0, 10, false).itens();
            List<Produto> produtos = produtoRepository.listarTodos(0, 50, false).itens();
            List<Servico> servicos = servicoRepository.listarTodos(0, 50, false).itens();

            if (clientes.isEmpty() || produtos.isEmpty() || servicos.isEmpty()) {
                return;
            }

            for (Cliente cliente : clientes) {
                List<Veiculo> veiculos = veiculoRepository.buscarPorClienteId(cliente.getId());
                if (!veiculos.isEmpty()) {
                    for (int i = 0; i < 2; i++) {
                        OrdemDeServico os = factory.create(cliente.getId(), veiculos.get(0).getId(), servicos, produtos);
                        repository.salvar(os);
                    }
                }
            }
        }
    }
}
