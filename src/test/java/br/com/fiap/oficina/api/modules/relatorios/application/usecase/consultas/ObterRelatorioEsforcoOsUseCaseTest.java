package br.com.fiap.oficina.api.modules.relatorios.application.usecase.consultas;

import br.com.fiap.oficina.api.modules.relatorios.api.dto.RelatorioEsforcoOsResponse;
import br.com.fiap.oficina.api.modules.relatorios.application.repository.RelatorioRepository;
import br.com.fiap.oficina.api.modules.relatorios.domain.RelatorioEsforcoOs;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObterRelatorioEsforcoOsUseCaseTest {

    @Mock
    private RelatorioRepository repository;

    @InjectMocks
    private ObterRelatorioEsforcoOsUseCaseImpl useCase;

    @Test
    @DisplayName("Deve retornar relatório de esforço para uma OS válida")
    void deveRetornarRelatorioEsforco() {
        UUID osId = UUID.randomUUID();
        RelatorioEsforcoOs relatorio = new RelatorioEsforcoOs(osId, 5, 120L);
        when(repository.buscarEsforcoPorOsId(osId)).thenReturn(Optional.of(relatorio));

        Optional<RelatorioEsforcoOsResponse> result = useCase.executar(osId);

        assertTrue(result.isPresent());
        assertEquals(osId, result.get().ordemDeServicoId());
        assertEquals(5, result.get().totalServicosRealizados());
        assertEquals(120L, result.get().esforcoTotalMinutos());
    }

    @Test
    @DisplayName("Deve retornar vazio quando OS não encontrada")
    void deveRetornarVazioQuandoNaoEncontrado() {
        UUID osId = UUID.randomUUID();
        when(repository.buscarEsforcoPorOsId(osId)).thenReturn(Optional.empty());

        Optional<RelatorioEsforcoOsResponse> result = useCase.executar(osId);

        assertFalse(result.isPresent());
    }
}
