package br.com.fiap.oficina.api.core.exception;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExceptionMappersTest {

    @Test
    @DisplayName("IllegalArgumentExceptionMapper deve retornar status 400")
    void illegalArgumentExceptionMapperTest() {
        IllegalArgumentExceptionMapper mapper = new IllegalArgumentExceptionMapper();
        IllegalArgumentException exception = new IllegalArgumentException("Erro de argumento");
        
        Response response = mapper.toResponse(exception);
        
        assertEquals(400, response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("NotFoundExceptionMapper deve retornar status 404")
    void notFoundExceptionMapperTest() {
        NotFoundExceptionMapper mapper = new NotFoundExceptionMapper();
        jakarta.ws.rs.NotFoundException exception = new jakarta.ws.rs.NotFoundException("Não encontrado");
        
        Response response = mapper.toResponse(exception);
        
        assertEquals(404, response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("ConcorrenciaExceptionMapper deve retornar status 409")
    void concorrenciaExceptionMapperTest() {
        ConcorrenciaExceptionMapper mapper = new ConcorrenciaExceptionMapper();
        jakarta.persistence.OptimisticLockException exception = new jakarta.persistence.OptimisticLockException("Concorrência");
        
        Response response = mapper.toResponse(exception);
        
        assertEquals(409, response.getStatus());
        assertNotNull(response.getEntity());
    }
}
