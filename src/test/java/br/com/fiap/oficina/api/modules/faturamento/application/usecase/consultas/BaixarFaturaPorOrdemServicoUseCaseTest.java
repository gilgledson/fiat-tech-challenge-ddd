package br.com.fiap.oficina.api.modules.faturamento.application.usecase.consultas;

import br.com.fiap.oficina.api.modules.faturamento.application.repository.FaturaRepository;
import br.com.fiap.oficina.api.modules.faturamento.domain.entity.Fatura;
import br.com.fiap.oficina.api.modules.faturamento.domain.valueObject.MetodoPagamento;
import br.com.fiap.oficina.api.modules.faturamento.domain.valueObject.StatusFatura;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BaixarFaturaPorOrdemServicoUseCaseTest {

    @Mock
    private FaturaRepository faturaRepository;

    @Mock
    private BaixarFaturaUseCase baixarFaturaUseCase;

    @InjectMocks
    private BaixarFaturaPorOrdemServicoUseCaseImpl useCase;

    @Test
    @DisplayName("Deve retornar fatura ao buscar por ordem de serviço existente")
    void deveBuscarFaturaPorOrdemServicoComSucesso() {
        UUID osId = UUID.randomUUID();
        Fatura fatura = new Fatura(UUID.randomUUID(), null, osId, null,
                new BigDecimal("300.00"), StatusFatura.PENDENTE, MetodoPagamento.PIX);

        when(faturaRepository.buscarPorOrdemServicoId(osId)).thenReturn(Optional.of(fatura));

        Fatura resultado = useCase.buscarPorOrdemServicoId(osId);

        assertEquals(fatura, resultado);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao buscar por ordem de serviço inexistente")
    void deveLancarExcecaoQuandoOrdemServicoNaoTemFatura() {
        UUID osId = UUID.randomUUID();
        when(faturaRepository.buscarPorOrdemServicoId(osId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.buscarPorOrdemServicoId(osId));
    }

    @Test
    @DisplayName("Deve executar download chamando baixarFaturaUseCase com id da fatura")
    void deveExecutarDownloadDaFatura() {
        UUID osId = UUID.randomUUID();
        UUID faturaId = UUID.randomUUID();
        Fatura fatura = new Fatura(faturaId, null, osId, null,
                new BigDecimal("300.00"), StatusFatura.PENDENTE, MetodoPagamento.PIX);
        byte[] pdf = new byte[]{1, 2, 3};

        when(faturaRepository.buscarPorOrdemServicoId(osId)).thenReturn(Optional.of(fatura));
        when(baixarFaturaUseCase.executar(faturaId)).thenReturn(pdf);

        byte[] resultado = useCase.executar(osId);

        assertArrayEquals(pdf, resultado);
        verify(baixarFaturaUseCase).executar(faturaId);
    }
}
