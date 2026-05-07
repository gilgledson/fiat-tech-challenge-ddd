package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.item;

import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.*;
import jakarta.ws.rs.NotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IniciarExecucaoServicoUseCaseTest {

    @Mock
    private OrdemDeServicoRepository repository;

    @InjectMocks
    private IniciarExecucaoServicoUseCaseImpl useCase;

    private OrdemDeServicoServicos criarServico(UUID osId, OrdemDeServicoServicoStatus status) {
        OrdemDeServicoServicos s = new OrdemDeServicoServicos(
                osId, UUID.randomUUID(), "Alinhamento", 1,
                BigDecimal.TEN, BigDecimal.TEN, status, TipoServico.PREVENTIVO);
        return s;
    }

    @Test
    @DisplayName("Deve iniciar execução de serviço APROVADO em OS APROVADA")
    void deveIniciarExecucaoDeServicoAprovadoEmOsAprovada() {
        UUID osId = UUID.randomUUID();
        UUID executorId = UUID.randomUUID();

        OrdemDeServicoServicos servico = criarServico(osId, OrdemDeServicoServicoStatus.APROVADO);
        UUID servicoId = servico.getId();

        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.APROVADA);
        os.setServicos(new ArrayList<>(List.of(servico)));

        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        useCase.executar(osId, servicoId, executorId);

        assertEquals(OrdemDeServicoServicoStatus.EM_EXECUCAO, servico.getStatus());
        assertNotNull(servico.getDataInicioExecucao());
        assertEquals(executorId, servico.getUsuarioExecutorId());
        verify(repository).atualizar(os);
    }

    @Test
    @DisplayName("Deve iniciar execução de serviço APROVADO em OS EM_EXECUCAO")
    void deveIniciarExecucaoEmOsEmExecucao() {
        UUID osId = UUID.randomUUID();
        OrdemDeServicoServicos servico = criarServico(osId, OrdemDeServicoServicoStatus.APROVADO);
        UUID servicoId = servico.getId();

        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.EM_EXECUCAO);
        os.setServicos(new ArrayList<>(List.of(servico)));

        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        useCase.executar(osId, servicoId, null);

        assertEquals(OrdemDeServicoServicoStatus.EM_EXECUCAO, servico.getStatus());
        verify(repository).atualizar(os);
    }

    @Test
    @DisplayName("Deve lançar exceção quando OS não for encontrada")
    void deveLancarExcecaoQuandoOsNaoEncontrada() {
        UUID osId = UUID.randomUUID();
        when(repository.buscarPorId(osId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> useCase.executar(osId, UUID.randomUUID(), null));
        verify(repository, never()).atualizar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando OS não estiver APROVADA ou EM_EXECUCAO")
    void deveLancarExcecaoQuandoStatusOsInvalido() {
        UUID osId = UUID.randomUUID();
        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.AGUARDANDO_APROVACAO);

        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> useCase.executar(osId, UUID.randomUUID(), null));
        assertTrue(ex.getMessage().contains("APROVADA"));
        verify(repository, never()).atualizar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando serviço não for encontrado na OS")
    void deveLancarExcecaoQuandoServicoNaoEncontrado() {
        UUID osId = UUID.randomUUID();
        OrdemDeServicoServicos servico = criarServico(osId, OrdemDeServicoServicoStatus.APROVADO);

        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.APROVADA);
        os.setServicos(new ArrayList<>(List.of(servico)));

        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        UUID idInexistente = UUID.randomUUID();
        assertThrows(NotFoundException.class,
                () -> useCase.executar(osId, idInexistente, null));
        verify(repository, never()).atualizar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando serviço não estiver APROVADO (PENDENTE)")
    void deveLancarExcecaoQuandoServicoNaoAprovado() {
        UUID osId = UUID.randomUUID();
        OrdemDeServicoServicos servico = criarServico(osId, OrdemDeServicoServicoStatus.PENDENTE);
        UUID servicoId = servico.getId();

        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.APROVADA);
        os.setServicos(new ArrayList<>(List.of(servico)));

        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> useCase.executar(osId, servicoId, null));
        assertTrue(ex.getMessage().contains("APROVADO"));
        verify(repository, never()).atualizar(any());
    }
}









