package br.com.fiap.oficina.api.modules.relatorios.application.usecase.consultas;

import br.com.fiap.oficina.api.modules.relatorios.api.dto.RelatorioTempoMedioServicoResponse;
import br.com.fiap.oficina.api.modules.relatorios.application.repository.RelatorioRepository;
import br.com.fiap.oficina.api.modules.relatorios.domain.RelatorioTempoMedioServico;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObterRelatorioTempoMedioServicoUseCaseTest {

    @Mock
    private RelatorioRepository repository;

    @InjectMocks
    private ObterRelatorioTempoMedioServicoUseCaseImpl useCase;

    @Test
    @DisplayName("Deve retornar lista de tempos médios de serviços")
    void deveRetornarListaTemposMedios() {
        UUID servicoId = UUID.randomUUID();
        RelatorioTempoMedioServico relatorio = new RelatorioTempoMedioServico(servicoId, "Troca de Óleo", 10, 45L);
        when(repository.buscarTemposMediosServicos()).thenReturn(List.of(relatorio));

        List<RelatorioTempoMedioServicoResponse> result = useCase.executar();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Troca de Óleo", result.get(0).nomeServico());
        assertEquals(45L, result.get(0).tempoMedioMinutos());
    }
}
