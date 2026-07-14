package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.infrastructure.seeder;

import br.com.fiap.oficina.api.modules.atendimento.cliente.application.repository.ClienteRepository;
import br.com.fiap.oficina.api.modules.atendimento.cliente.domain.entity.Cliente;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.repository.VeiculoRepository;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.domain.entity.Veiculo;
import br.com.fiap.oficina.api.modules.catalogo.produto.application.repository.ProdutoRepository;
import br.com.fiap.oficina.api.modules.catalogo.produto.domain.entity.Produto;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.repository.ServicoRepository;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.Servico;
import br.com.fiap.oficina.api.modules.operacional.funcionario.application.repository.FuncionarioRepository;
import br.com.fiap.oficina.api.modules.operacional.funcionario.domain.entity.Funcionario;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.shared.infrastructure.seeder.Seeder;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import lombok.RequiredArgsConstructor;
import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class OrdemServicoSeeder implements Seeder {

    @ConfigProperty(name = "quarkus.profile")
    private String ambiente;

    private final OrdemDeServicoRepository repository;
    private final OrdemServicoFactory factory;

    private final ClienteRepository clienteRepository;
    private final VeiculoRepository veiculoRepository;
    private final ProdutoRepository produtoRepository;
    private final ServicoRepository servicoRepository;
    private final FuncionarioRepository funcionarioRepository;

    @Override
    public int ordem() {
        // Depende de clientes/veículos (AtendimentoSeeder), produtos/serviços
        // (CatalogoSeeder) e funcionários (FuncionarioSeeder) já existirem.
        return 100;
    }

    @Override
    public void execute() {
        if ("prod".equals(ambiente)) {
            return;
        }

        if (repository.buscarTodas(null, null, 0, 1, false).totalElementos() == 0) {
            List<Cliente> clientes = clienteRepository.listarTodos(0, 50, false).itens();
            List<Produto> produtos = produtoRepository.listarTodos(0, 100, false).itens();
            List<Servico> servicos = servicoRepository.listarTodos(0, 100, false).itens();
            List<Funcionario> funcionarios = funcionarioRepository.listarTodos(0, 50, false).itens();

            if (clientes.isEmpty() || produtos.isEmpty() || servicos.isEmpty()) {
                return;
            }

            java.security.SecureRandom random = new java.security.SecureRandom();
            
            // Gera 50 ordens de serviço distribuídas nos últimos 90 dias
            for (int i = 0; i < 50; i++) {
                Cliente cliente = clientes.get(random.nextInt(clientes.size()));
                List<Veiculo> veiculos = veiculoRepository.buscarPorClienteId(cliente.getId());
                
                if (veiculos.isEmpty()) continue;
                
                Veiculo veiculo = veiculos.get(random.nextInt(veiculos.size()));
                
                // Varias datas nos últimos 90 dias
                java.time.LocalDateTime dataAbertura = java.time.LocalDateTime.now()
                    .minusDays(random.nextInt(90))
                    .minusHours(random.nextInt(24))
                    .minusMinutes(random.nextInt(60));

                OrdemDeServico os = factory.create(
                    cliente.getId(), 
                    veiculo.getId(), 
                    servicos, 
                    produtos, 
                    funcionarios, 
                    dataAbertura
                );
                
                repository.salvar(os);
            }
        }
    }
}
