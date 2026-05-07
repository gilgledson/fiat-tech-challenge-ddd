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
class FinalizarExecucaoServicoUseCaseTest {

    @Mock
    private OrdemDeServicoRepository repository;

    @InjectMocks
    private FinalizarExecucaoServicoUseCaseImpl useCase;

    private OrdemDeServicoServicos criarServico(UUID osId, OrdemDeServicoServicoStatus status) {
        return new OrdemDeServicoServicos(
                osId, UUID.randomUUID(), "Balanceamento", 1,
                BigDecimal.TEN, BigDecimal.TEN, status, TipoServico.PREVENTIVO);
    }

    @Test
    @DisplayName("Deve finalizar execução de serviço EM_EXECUCAO com sucesso")
    void deveFinalizarExecucaoComSucesso() {
        UUID osId = UUID.randomUUID();
        OrdemDeServicoServicos servico = criarServico(osId, OrdemDeServicoServicoStatus.EM_EXECUCAO);
        UUID servicoId = servico.getId();

        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.EM_EXECUCAO);
        os.setServicos(new ArrayList<>(List.of(servico)));

        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        useCase.executar(osId, servicoId);

        assertEquals(OrdemDeServicoServicoStatus.FINALIZADO, servico.getStatus());
        assertNotNull(servico.getDataFimExecucao());
        verify(repository).atualizar(os);
    }

    @Test
    @DisplayName("Deve lançar exceção quando OS não for encontrada")
    void deveLancarExcecaoQuandoOsNaoEncontrada() {
        UUID osId = UUID.randomUUID();
        when(repository.buscarPorId(osId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> useCase.executar(osId, UUID.randomUUID()));
        verify(repository, never()).atualizar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando OS não estiver EM_EXECUCAO")
    void deveLancarExcecaoQuandoOsNaoEmExecucao() {
        UUID osId = UUID.randomUUID();
        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.APROVADA);

        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> useCase.executar(osId, UUID.randomUUID()));
        assertTrue(ex.getMessage().contains("EM_EXECUCAO"));
        verify(repository, never()).atualizar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando serviço não for encontrado")
    void deveLancarExcecaoQuandoServicoNaoEncontrado() {
        UUID osId = UUID.randomUUID();
        OrdemDeServicoServicos servico = criarServico(osId, OrdemDeServicoServicoStatus.EM_EXECUCAO);

        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.EM_EXECUCAO);
        os.setServicos(new ArrayList<>(List.of(servico)));

        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        assertThrows(NotFoundException.class,
                () -> useCase.executar(osId, UUID.randomUUID()));
        verify(repository, never()).atualizar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando serviço não estiver EM_EXECUCAO")
    void deveLancarExcecaoQuandoServicoNaoEmExecucao() {
        UUID osId = UUID.randomUUID();
        OrdemDeServicoServicos servico = criarServico(osId, OrdemDeServicoServicoStatus.APROVADO);
        UUID servicoId = servico.getId();

        OrdemDeServico os = new OrdemDeServico();
        os.setId(osId);
        os.setStatus(OrdemDeServicoStatus.EM_EXECUCAO);
        os.setServicos(new ArrayList<>(List.of(servico)));

        when(repository.buscarPorId(osId)).thenReturn(Optional.of(os));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> useCase.executar(osId, servicoId));
        assertTrue(ex.getMessage().contains("EM_EXECUCAO"));
        verify(repository, never()).atualizar(any());
    }
}









