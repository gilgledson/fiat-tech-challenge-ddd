package br.com.fiap.oficina.api.modules.operacional.funcionario.infrastructure.seeder;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import br.com.fiap.oficina.api.modules.identidade.application.repository.UsuarioRepository;
import br.com.fiap.oficina.api.modules.identidade.domain.entity.Usuario;
import br.com.fiap.oficina.api.modules.identidade.domain.valueobject.PerfilUsuario;
import br.com.fiap.oficina.api.modules.identidade.infrastructure.seeder.UsuarioFactory;
import br.com.fiap.oficina.api.modules.operacional.funcionario.application.repository.FuncionarioRepository;
import br.com.fiap.oficina.api.modules.operacional.funcionario.domain.valueObject.CargoFuncionario;
import br.com.fiap.oficina.api.shared.infrastructure.seeder.Seeder;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class FuncionarioSeeder implements Seeder {

    @ConfigProperty(name = "quarkus.profile")
    private String ambiente;

    private final FuncionarioRepository repository;

    private final FuncionarioFactory factory;

    private final UsuarioRepository usuarioRepository;

    private final UsuarioFactory usuarioFactory;

    @Override
    public void execute() {
        if (ambiente.equals("prod")) {
            return;
        }

        if (repository.listarTodos(0, 1, false).totalElementos() == 0) {
            Usuario usuario = usuarioFactory.create("mecanico.chefe@oficina.com.br", PerfilUsuario.MECANICO);
            usuarioRepository.salvar(usuario);

            repository.salvar(factory.create(usuario.getId(), CargoFuncionario.MECANICO));

            Usuario gerente = usuarioFactory.create("gerente@oficina.com.br", PerfilUsuario.ADMIN);
            usuarioRepository.salvar(gerente);

            repository.salvar(factory.create(gerente.getId(), CargoFuncionario.ADMINISTRADOR));

            Usuario administrador = usuarioFactory.create("administrador@oficina.com.br", PerfilUsuario.ADMIN);
            usuarioRepository.salvar(administrador);

            repository.salvar(factory.create(administrador.getId(), CargoFuncionario.ADMINISTRADOR));
        }
    }
}
