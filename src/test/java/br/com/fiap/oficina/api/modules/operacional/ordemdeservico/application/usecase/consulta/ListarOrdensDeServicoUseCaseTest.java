package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.consulta;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.OrdemDeServicoOutput;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoStatus;
import br.com.fiap.oficina.api.shared.domain.entity.Pagina;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarOrdensDeServicoUseCaseTest {

    @Mock
    private OrdemDeServicoRepository repository;

    @InjectMocks
    private ListarOrdensDeServicoUseCaseImpl useCase;

    @Test
    @DisplayName("Deve retornar página com ordens de serviço")
    void deveRetornarPaginaComOutputs() {
        UUID osId = UUID.randomUUID();
        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.ABERTA);
        os.setServicos(new ArrayList<>());

        Pagina<OrdemDeServico> paginaMock = new Pagina<>(List.of(os), 0, 10, 1, 1L);
        when(repository.buscarTodas(any(), any(), anyInt(), anyInt(), anyBoolean()))
                .thenReturn(paginaMock);

        Pagina<OrdemDeServicoOutput> resultado = useCase.executar(null, null, 0, 10, false);

        assertNotNull(resultado);
        assertEquals(1, resultado.itens().size());
        assertEquals(osId, resultado.itens().get(0).id());
        assertEquals(0, resultado.paginaAtual());
        assertEquals(10, resultado.tamanhoPagina());
        assertEquals(1, resultado.totalPaginas());
        assertEquals(1L, resultado.totalElementos());
    }

    @Test
    @DisplayName("Deve retornar página vazia quando não houver ordens de serviço")
    void deveRetornarPaginaVaziaQuandoNaoHouverOrdens() {
        Pagina<OrdemDeServico> paginaVazia = new Pagina<>(List.of(), 0, 10, 0, 0L);
        when(repository.buscarTodas(any(), any(), anyInt(), anyInt(), anyBoolean()))
                .thenReturn(paginaVazia);

        Pagina<OrdemDeServicoOutput> resultado = useCase.executar(null, null, 0, 10, false);

        assertNotNull(resultado);
        assertTrue(resultado.itens().isEmpty());
        assertEquals(0L, resultado.totalElementos());
    }
}









