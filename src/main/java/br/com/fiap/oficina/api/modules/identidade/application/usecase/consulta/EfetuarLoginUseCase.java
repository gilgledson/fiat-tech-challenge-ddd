package br.com.fiap.oficina.api.modules.identidade.application.usecase.consulta;

import br.com.fiap.oficina.api.modules.identidade.api.dto.TokenResponse;

public interface EfetuarLoginUseCase {

    TokenResponse executar(String email, String senha);

}






