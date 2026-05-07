package br.com.fiap.oficina.api.modules.identidade.infrastructure.persistence;

import br.com.fiap.oficina.api.modules.identidade.application.repository.UsuarioRepository;
import br.com.fiap.oficina.api.modules.identidade.domain.entity.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class PanacheUsuarioRepositoryImpl implements UsuarioRepository, PanacheRepositoryBase<UsuarioJpaEntity, UUID> {
    @Override
    public void salvar(Usuario usuario) {
        UsuarioJpaEntity usuarioJpa = new UsuarioJpaEntity();
        usuarioJpa.setId(usuario.getId());
        usuarioJpa.setAtivo(usuario.isAtivo());
        usuarioJpa.setEmail(usuario.getEmail());
        usuarioJpa.setSenhaHash(usuario.getSenhaHash());
        usuarioJpa.setPerfil(usuario.getPerfil());
        getEntityManager().merge(usuarioJpa);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return find("email", email).firstResultOptional()
                .map(jpa -> {
                    Usuario domain = new Usuario(
                            jpa.getId(),
                            jpa.getEmail(),
                            jpa.getSenhaHash(),
                            jpa.getPerfil(),
                            jpa.isAtivo());
                    return domain;
                });
    }

    @Override
    public Optional<Usuario> buscarPorId(UUID id) {
        return find("id", id).firstResultOptional()
                .map(jpa -> {
                    Usuario domain = new Usuario(
                            jpa.getId(),
                            jpa.getEmail(),
                            jpa.getSenhaHash(),
                            jpa.getPerfil(),
                            jpa.isAtivo());
                    return domain;
                });
    }
}






