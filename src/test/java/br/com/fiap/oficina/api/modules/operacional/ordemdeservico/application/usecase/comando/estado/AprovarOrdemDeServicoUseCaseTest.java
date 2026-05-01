package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.estado;

import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.api.dto.AprovarServicoRequest;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicoStatus;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicos;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoStatus;
import io.vertx.core.eventbus.EventBus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AprovarOrdemDeServicoUseCaseTest {

    @Mock
    private OrdemDeServicoRepository repository;

    @Mock
    private EventBus eventBus;

    @InjectMocks
    private AprovarOrdemDeServicoUseCaseImpl useCase;

    @Test
    @DisplayName("Deve lançar exceção ao tentar rejeitar um serviço corretivo")
    void deveLancarExcecaoAoRejeitarServicoCorretivo() {
        // Arrange
        UUID osId = UUID.randomUUID();
        UUID servicoCorretivoId = UUID.randomUUID();
        
        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.AGUARDANDO_APROVACAO);
        
        List<OrdemDeServicoServicos> servicos = new ArrayList<>();
        servicos.add(new OrdemDeServicoServicos(osId, servicoCorretivoId, "Freio", 1, BigDecimal.TEN, BigDecimal.TEN, OrdemDeServicoServicoStatus.PENDENTE, TipoServico.CORRETIVO));
        os.setServicos(servicos);

        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        AprovarServicoRequest request = new AprovarServicoRequest(
                List.of(), // aprovados
                List.of(servicoCorretivoId) // rejeitados
        );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            useCase.executar(osId, request);
        });

        assertTrue(exception.getMessage().contains("não pode ser rejeitado"));
    }
}
