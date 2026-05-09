package br.com.fiap.oficina.api.modules.catalogo.infrastructure.seeder;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import br.com.fiap.oficina.api.modules.catalogo.produto.application.repository.ProdutoRepository;
import br.com.fiap.oficina.api.modules.catalogo.produto.infrastructure.seeder.ProdutoFactory;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.repository.ServicoRepository;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;
import br.com.fiap.oficina.api.modules.catalogo.servico.infrastructure.seeder.ServicoFactory;
import br.com.fiap.oficina.api.shared.infrastructure.seeder.Seeder;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class CatalogoSeeder implements Seeder {

    @ConfigProperty(name = "quarkus.profile")
    private String ambiente;

    private final ProdutoRepository produtoRepository;
    private final ProdutoFactory produtoFactory;
    private final ServicoRepository servicoRepository;
    private final ServicoFactory servicoFactory;

    @Override
    public void execute() {
        if ("prod".equals(ambiente)) {
            return;
        }
        if (produtoRepository.listarTodos(0, 1, false).totalElementos() == 0) {
            for (int i = 0; i < 20; i++) {
                produtoRepository.salvar(produtoFactory.create());
            }
        }

        if (servicoRepository.listarTodos(0, 1, false).totalElementos() == 0) {
            for (int i = 0; i < 4; i++) {
                servicoRepository.salvar(servicoFactory.create(TipoServico.PREVENTIVO));
                servicoRepository.salvar(servicoFactory.create(TipoServico.CORRETIVO));
            }
        }
    }
}
