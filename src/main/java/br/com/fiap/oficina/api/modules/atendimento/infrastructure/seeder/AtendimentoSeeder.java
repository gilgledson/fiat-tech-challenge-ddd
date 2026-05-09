package br.com.fiap.oficina.api.modules.atendimento.infrastructure.seeder;

import br.com.fiap.oficina.api.modules.atendimento.cliente.application.repository.ClienteRepository;
import br.com.fiap.oficina.api.modules.atendimento.cliente.domain.entity.Cliente;
import br.com.fiap.oficina.api.modules.atendimento.cliente.infrastructure.seeder.ClienteFactory;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.repository.VeiculoRepository;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.infrastructure.seeder.VeiculoFactory;
import br.com.fiap.oficina.api.modules.identidade.application.repository.UsuarioRepository;
import br.com.fiap.oficina.api.modules.identidade.domain.entity.Usuario;
import br.com.fiap.oficina.api.modules.identidade.domain.valueobject.PerfilUsuario;
import br.com.fiap.oficina.api.modules.identidade.infrastructure.seeder.UsuarioFactory;
import br.com.fiap.oficina.api.shared.infrastructure.seeder.Seeder;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class AtendimentoSeeder implements Seeder {

    @ConfigProperty(name = "quarkus.profile")
    private String ambiente;

    private final ClienteRepository clienteRepository;
    private final ClienteFactory clienteFactory;
    private final VeiculoRepository veiculoRepository;
    private final VeiculoFactory veiculoFactory;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioFactory usuarioFactory;

    @Override
    public void execute() {
        if ("prod".equals(ambiente)) {
            return;
        }

        if (clienteRepository.listarTodos(0, 1, false).totalElementos() == 0) {
            // Cria alguns clientes de teste
            for (int i = 0; i < 15; i++) {
                UUID usuarioId = null;
                // Cria usuário apenas para os 3 primeiros clientes
                if (i < 3) {
                    Usuario usuario = usuarioFactory.create(PerfilUsuario.CLIENTE);
                    usuarioRepository.salvar(usuario);
                    usuarioId = usuario.getId();
                }

                Cliente cliente = clienteFactory.create(usuarioId);
                clienteRepository.salvar(cliente);

                // Cria um veículo para cada cliente
                veiculoRepository.salvar(veiculoFactory.create(cliente.getId()));
            }
        }
    }
}
