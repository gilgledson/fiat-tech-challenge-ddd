package br.com.fiap.oficina.api.modules.identidade.application.usecase.comandos;

import br.com.fiap.oficina.api.modules.identidade.application.repository.UsuarioRepository;
import br.com.fiap.oficina.api.modules.identidade.application.security.PasswordEncoder;
import br.com.fiap.oficina.api.modules.identidade.domain.valueobject.PerfilUsuario;
import br.com.fiap.oficina.api.modules.identidade.domain.entity.Usuario;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CriarUsuarioUseCaseImpl implements CriarUsuarioUseCase {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Usuario executar(String email, String senhaPura, PerfilUsuario perfil) {
        if (repository.buscarPorEmail(email).isPresent()) {
            throw new IllegalArgumentException("Já existe um usuário com este e-mail.");
        }

        String senhaCriptografada = passwordEncoder.criptografar(senhaPura);
        Usuario novoUsuario = new Usuario(email, senhaCriptografada, perfil);

        repository.salvar(novoUsuario);

        return novoUsuario;
    }
}






