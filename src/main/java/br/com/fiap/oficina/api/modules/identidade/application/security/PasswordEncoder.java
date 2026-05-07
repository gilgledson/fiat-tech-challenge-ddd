package br.com.fiap.oficina.api.modules.identidade.application.security;

public interface PasswordEncoder {
    public boolean verificar(String senhaPura, String hashSenha);

    public String criptografar(String senhaPura);
}






