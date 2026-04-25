package br.com.fiap.oficina.api.modules.identidade.application.security;

public interface PasswordEncoder {
    public String criptografar(String senhaPura);
}
