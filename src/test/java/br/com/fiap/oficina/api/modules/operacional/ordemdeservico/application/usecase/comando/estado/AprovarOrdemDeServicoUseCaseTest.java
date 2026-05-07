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
        OrdemDeServicoServicos servicoCorretivo = new OrdemDeServicoServicos(osId, servicoCorretivoId, "Freio", 1, BigDecimal.TEN, BigDecimal.TEN, OrdemDeServicoServicoStatus.PENDENTE, TipoServico.CORRETIVO);
        servicos.add(servicoCorretivo);
        os.setServicos(servicos);

        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        AprovarServicoRequest request = new AprovarServicoRequest(
                List.of(), // aprovados
                List.of(servicoCorretivo.getId()) // rejeitados - ID da instância, não do catálogo
        );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            useCase.executar(osId, request);
        });

        assertTrue(exception.getMessage().contains("não pode ser rejeitado"));
    }

    @Test
    @DisplayName("Deve permitir aprovação de serviço extra quando a OS já está em execução")
    void devePermitirAprovacaoDeServicoExtraEmExecucao() {
        // Arrange
        UUID osId = UUID.randomUUID();
        UUID servicoExtraId = UUID.randomUUID();
        
        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.EM_EXECUCAO);
        
        List<OrdemDeServicoServicos> servicos = new ArrayList<>();
        // Serviço já aprovado e em execução
        servicos.add(new OrdemDeServicoServicos(osId, UUID.randomUUID(), "Serviço 1", 1, BigDecimal.TEN, BigDecimal.TEN, OrdemDeServicoServicoStatus.EM_EXECUCAO, TipoServico.PREVENTIVO));
        // Serviço extra pendente
        OrdemDeServicoServicos servicoExtra = new OrdemDeServicoServicos(osId, servicoExtraId, "Serviço Extra", 1, BigDecimal.TEN, BigDecimal.TEN, OrdemDeServicoServicoStatus.PENDENTE, TipoServico.PREVENTIVO);
        servicos.add(servicoExtra);
        os.setServicos(servicos);

        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        AprovarServicoRequest request = new AprovarServicoRequest(
                List.of(servicoExtra.getId()), // ID da instância, não do catálogo
                List.of()
        );

        // Act
        useCase.executar(osId, request);

        // Assert
        assertTrue(servicoExtra.getStatus() == OrdemDeServicoServicoStatus.APROVADO);
        assertTrue(os.getStatus() == OrdemDeServicoStatus.EM_EXECUCAO);
    }
}









