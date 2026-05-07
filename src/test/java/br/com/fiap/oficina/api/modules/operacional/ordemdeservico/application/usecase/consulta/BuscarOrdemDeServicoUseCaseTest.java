package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.consulta;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.OrdemDeServicoOutput;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoStatus;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarOrdemDeServicoUseCaseTest {

    @Mock
    private OrdemDeServicoRepository repository;

    @InjectMocks
    private BuscarOrdemDeServicoUseCaseImpl useCase;

    @Test
    @DisplayName("Deve retornar OS quando encontrada")
    void deveRetornarOutputQuandoOsEncontrada() {
        UUID id = UUID.randomUUID();
        OrdemDeServico os = new OrdemDeServico();
        os.setId(id);
        os.setStatus(OrdemDeServicoStatus.ABERTA);
        os.setServicos(new ArrayList<>());

        when(repository.buscarPorId(id)).thenReturn(Optional.of(os));

        OrdemDeServicoOutput resultado = useCase.executar(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.id());
        assertEquals(OrdemDeServicoStatus.ABERTA, resultado.status());
    }

    @Test
    @DisplayName("Deve lançar NotFoundException quando OS não for encontrada")
    void deveLancarExcecaoQuandoOsNaoEncontrada() {
        UUID id = UUID.randomUUID();
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.executar(id));
    }
}









