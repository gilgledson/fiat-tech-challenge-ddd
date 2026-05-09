package br.com.fiap.oficina.api.modules.identidade.infrastructure.seeder;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import br.com.fiap.oficina.api.modules.identidade.domain.entity.Usuario;
import br.com.fiap.oficina.api.modules.identidade.application.security.PasswordEncoder;
import br.com.fiap.oficina.api.modules.identidade.application.repository.UsuarioRepository;
import br.com.fiap.oficina.api.modules.identidade.domain.valueobject.PerfilUsuario;
import br.com.fiap.oficina.api.shared.infrastructure.seeder.Seeder;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class IdentidadeSeeder implements Seeder {

    @ConfigProperty(name = "quarkus.profile")
    private String ambiente;

    private final UsuarioRepository repository;

    private final UsuarioFactory factory;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void execute() {
        if ("prod".equals(ambiente)) {
            return;
        }
        // Garante usuário admin padrão
        repository.buscarPorEmail("admin@oficina.com.br").ifPresentOrElse(
                admin -> {
                    // Resetar senha em dev para garantir que o Newman passe com admin123
                    // IMPORTANTE: Manter o mesmo ID para não violar unique constraints
                    Usuario atualizado = new Usuario(
                            admin.getId(),
                            admin.getEmail(),
                            passwordEncoder.criptografar("admin123"),
                            PerfilUsuario.ADMIN,
                            true);
                    repository.salvar(atualizado);
                    System.out.println("✅ [Identidade] Usuário admin atualizado/verificado.");
                },
                () -> {
                    System.out.println("👤 [Identidade] Criando usuário admin padrão...");
                    repository.salvar(factory.create("admin@oficina.com.br", "admin123", PerfilUsuario.ADMIN));
                });

        if (repository.buscarPorEmail("mecanico@oficina.com.br").isEmpty()) {
            repository.salvar(factory.create("mecanico@oficina.com.br", PerfilUsuario.MECANICO));
        }

        // Cria alguns usuários aleatórios
        for (int i = 0; i < 5; i++) {
            repository.salvar(factory.create(PerfilUsuario.CLIENTE));
        }
    }
}
