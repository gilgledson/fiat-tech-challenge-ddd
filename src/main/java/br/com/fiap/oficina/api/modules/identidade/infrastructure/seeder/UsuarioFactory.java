package br.com.fiap.oficina.api.modules.identidade.infrastructure.seeder;

import br.com.fiap.oficina.api.modules.identidade.domain.entity.Usuario;
import br.com.fiap.oficina.api.modules.identidade.domain.valueobject.PerfilUsuario;
import br.com.fiap.oficina.api.modules.identidade.application.security.PasswordEncoder;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;

@ApplicationScoped
@RequiredArgsConstructor
public class UsuarioFactory {

    private final PasswordEncoder passwordEncoder;

    private final Faker faker = new Faker();

    public Usuario create(PerfilUsuario perfil) {
        String email = faker.internet().emailAddress();
        String senhaHash = passwordEncoder.criptografar("senha123");
        return new Usuario(email, senhaHash, perfil);
    }

    public Usuario create(String email, String senha, PerfilUsuario perfil) {
        String senhaHash = passwordEncoder.criptografar(senha);
        return new Usuario(email, senhaHash, perfil);
    }

    public Usuario create(String email, PerfilUsuario perfil) {
        return create(email, "senha123", perfil);
    }
}
