package br.com.fiap.oficina.api.modules.faturamento.application.usecase.comandos;

import br.com.fiap.oficina.api.modules.faturamento.application.repository.FaturaRepository;
import br.com.fiap.oficina.api.modules.faturamento.domain.entity.Fatura;
import br.com.fiap.oficina.api.modules.faturamento.domain.valueObject.MetodoPagamento;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CriarFaturaUseCaseTest {

    @Mock
    private FaturaRepository faturaRepository;

    @InjectMocks
    private CriarFaturaUseCaseImpl useCase;

    @Test
    @DisplayName("Deve salvar fatura no repositório")
    void deveSalvarFatura() {
        Fatura fatura = new Fatura(UUID.randomUUID(), new BigDecimal("500.00"), MetodoPagamento.PIX);

        useCase.execute(fatura);

        verify(faturaRepository).salvar(fatura);
    }
}
