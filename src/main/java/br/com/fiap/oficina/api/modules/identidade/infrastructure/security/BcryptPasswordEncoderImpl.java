package br.com.fiap.oficina.api.modules.identidade.infrastructure.security;

import br.com.fiap.oficina.api.modules.identidade.application.security.PasswordEncoder;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BcryptPasswordEncoderImpl implements PasswordEncoder {
    @Override
    public String criptografar(String senhaPura) {
        return BcryptUtil.bcryptHash(senhaPura);
    }
}
