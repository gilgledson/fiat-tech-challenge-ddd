package br.com.fiap.oficina.api.core.exception;

import jakarta.persistence.OptimisticLockException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider // Diz para o Quarkus registrar esse interceptador
public class ConcorrenciaExceptionMapper implements ExceptionMapper<OptimisticLockException> {

    @Override
    public Response toResponse(OptimisticLockException exception) {
        // Retorna HTTP 409 (Conflict) com uma mensagem amigável para o Front-end
        return Response.status(Response.Status.CONFLICT)
                .entity(new ErroResponse(
                        "CONFLITO_ESTOQUE",
                        "Outro usuário acabou de atualizar este produto. Por favor, atualize a página e tente novamente."))
                .build();
    }

    // Record interno só para formatar o JSON de saída
    private record ErroResponse(String codigo, String mensagem) {
    }
}






