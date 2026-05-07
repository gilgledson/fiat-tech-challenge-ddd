package br.com.fiap.oficina.api.modules.orcamento.application.usecase.comandos;

import br.com.fiap.oficina.api.modules.orcamento.application.repository.OrcamentoRepository;
import br.com.fiap.oficina.api.modules.orcamento.domain.entity.Orcamento;
import br.com.fiap.oficina.api.modules.orcamento.domain.valueObject.MetodoPagamento;
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
class CriarOrcamentoUseCaseTest {

    @Mock
    private OrcamentoRepository orcamentoRepository;

    @InjectMocks
    private CriarOrcamentoUseCaseImpl useCase;

    @Test
    @DisplayName("Deve salvar Orcamento no repositório")
    void deveSalvarFatura() {
        Orcamento orcamento = new Orcamento(UUID.randomUUID(), new BigDecimal("500.00"), MetodoPagamento.PIX);

        useCase.execute(orcamento);

        verify(orcamentoRepository).salvar(orcamento);
    }
}

