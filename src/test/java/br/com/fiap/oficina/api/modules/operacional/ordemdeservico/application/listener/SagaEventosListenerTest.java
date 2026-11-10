package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.listener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.api.dto.AprovarServicoRequest;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event.EventosExternos;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.event.OrdemServicoPaga;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.ciclovida.CancelarOrdemDeServicoUseCase;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.ciclovida.ConcluirExecucaoOrdemDeServicoUseCase;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.estado.AprovarOrdemDeServicoUseCase;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.estado.ConcluirDiagnosticoUseCase;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.estado.IniciarDiagnosticoUseCase;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.estado.IniciarExecucaoOsUseCase;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.estado.RejeitarOrcamentoUseCase;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.item.FinalizarExecucaoServicoUseCase;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.comando.item.IniciarExecucaoServicoUseCase;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServico;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicoStatus;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoServicos;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.domain.entity.OrdemDeServicoStatus;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import jakarta.ws.rs.NotFoundException;

@ExtendWith(MockitoExtension.class)
class SagaEventosListenerTest {

    @Mock
    private OrdemDeServicoRepository repository;
    @Mock
    private IniciarDiagnosticoUseCase iniciarDiagnostico;
    @Mock
    private ConcluirDiagnosticoUseCase concluirDiagnostico;
    @Mock
    private AprovarOrdemDeServicoUseCase aprovar;
    @Mock
    private RejeitarOrcamentoUseCase rejeitar;
    @Mock
    private IniciarExecucaoOsUseCase iniciarExecucao;
    @Mock
    private IniciarExecucaoServicoUseCase iniciarServico;
    @Mock
    private FinalizarExecucaoServicoUseCase finalizarServico;
    @Mock
    private ConcluirExecucaoOrdemDeServicoUseCase concluirExecucao;
    @Mock
    private CancelarOrdemDeServicoUseCase cancelar;
    @Mock
    private EventBus eventBus;

    @InjectMocks
    private SagaEventosListener listener;

    private final UUID osId = UUID.randomUUID();
    private final UUID servicoId = UUID.randomUUID();

    private OrdemDeServico ordem(OrdemDeServicoStatus status) {
        OrdemDeServico ordem = new OrdemDeServico();
        ordem.setId(osId);
        ordem.setStatus(status);
        ordem.setServicos(new ArrayList<>());
        when(repository.buscarPorId(osId)).thenReturn(Optional.of(ordem));
        return ordem;
    }

    private OrdemDeServico ordemComServico(OrdemDeServicoStatus status, OrdemDeServicoServicoStatus statusServico) {
        OrdemDeServico ordem = ordem(status);
        OrdemDeServicoServicos servico = new OrdemDeServicoServicos(osId, UUID.randomUUID(), "Troca de óleo", 1,
                BigDecimal.TEN, BigDecimal.TEN, statusServico, TipoServico.PREVENTIVO);
        servico.setId(servicoId);
        ordem.getServicos().add(servico);
        return ordem;
    }

    private JsonObject evento() {
        return new JsonObject().put("ordemServicoId", osId.toString());
    }

    private JsonObject eventoDeServico() {
        return evento().put("servicoOsId", servicoId.toString());
    }

    // ------------------------------------------------------------ Execution

    @Test
    @DisplayName("Diagnóstico iniciado: OS aberta passa para em diagnóstico")
    void deveIniciarDiagnostico() {
        ordem(OrdemDeServicoStatus.ABERTA);

        listener.onDiagnosticoIniciado(evento());

        verify(iniciarDiagnostico).executar(osId);
    }

    @Test
    @DisplayName("Diagnóstico iniciado repetido é ignorado")
    void deveIgnorarDiagnosticoIniciadoRepetido() {
        ordem(OrdemDeServicoStatus.EM_DIAGNOSTICO);

        listener.onDiagnosticoIniciado(evento());

        verifyNoInteractions(iniciarDiagnostico);
    }

    @ParameterizedTest
    @EnumSource(value = OrdemDeServicoStatus.class, names = { "ABERTA", "EM_DIAGNOSTICO" })
    @DisplayName("Diagnóstico concluído: OS segue para aguardando aprovação")
    void deveConcluirDiagnostico(OrdemDeServicoStatus status) {
        ordem(status);

        listener.onDiagnosticoConcluido(evento());

        verify(concluirDiagnostico).executar(osId);
    }

    @Test
    @DisplayName("Diagnóstico concluído repetido é ignorado")
    void deveIgnorarDiagnosticoConcluidoRepetido() {
        ordem(OrdemDeServicoStatus.AGUARDANDO_APROVACAO);

        listener.onDiagnosticoConcluido(evento());

        verifyNoInteractions(concluirDiagnostico);
    }

    @Test
    @DisplayName("Execução iniciada: só age em OS aprovada")
    void deveIniciarExecucao() {
        ordem(OrdemDeServicoStatus.APROVADA);
        listener.onExecucaoIniciada(evento());
        verify(iniciarExecucao).executar(osId);

        ordem(OrdemDeServicoStatus.EM_EXECUCAO);
        listener.onExecucaoIniciada(evento());
        verify(iniciarExecucao).executar(osId); // continua uma única chamada
    }

    @Test
    @DisplayName("Serviço iniciado: registra o executor informado pelo Execution Service")
    void deveIniciarServico() {
        UUID executor = UUID.randomUUID();
        ordemComServico(OrdemDeServicoStatus.EM_EXECUCAO, OrdemDeServicoServicoStatus.APROVADO);

        listener.onServicoIniciado(eventoDeServico().put("usuarioExecutorId", executor.toString()));

        verify(iniciarServico).executar(osId, servicoId, executor);
    }

    @Test
    @DisplayName("Serviço iniciado sem executor e serviço iniciado repetido")
    void deveTratarServicoIniciadoSemExecutorERepetido() {
        ordemComServico(OrdemDeServicoStatus.EM_EXECUCAO, OrdemDeServicoServicoStatus.APROVADO);
        listener.onServicoIniciado(eventoDeServico());
        verify(iniciarServico).executar(osId, servicoId, null);

        ordemComServico(OrdemDeServicoStatus.EM_EXECUCAO, OrdemDeServicoServicoStatus.EM_EXECUCAO);
        listener.onServicoIniciado(eventoDeServico());
        verify(iniciarServico).executar(any(), any(), any()); // continua uma única chamada
    }

    @Test
    @DisplayName("Serviço finalizado: só age em serviço em execução")
    void deveFinalizarServico() {
        ordemComServico(OrdemDeServicoStatus.EM_EXECUCAO, OrdemDeServicoServicoStatus.EM_EXECUCAO);
        listener.onServicoFinalizado(eventoDeServico());
        verify(finalizarServico).executar(osId, servicoId);

        ordemComServico(OrdemDeServicoStatus.EM_EXECUCAO, OrdemDeServicoServicoStatus.FINALIZADO);
        listener.onServicoFinalizado(eventoDeServico());
        verify(finalizarServico).executar(any(), any()); // continua uma única chamada
    }

    @Test
    @DisplayName("Evento de serviço que não pertence à OS deve falhar (vai para a DLQ)")
    void deveFalharParaServicoDesconhecido() {
        ordem(OrdemDeServicoStatus.EM_EXECUCAO);

        assertThrows(NotFoundException.class, () -> listener.onServicoIniciado(eventoDeServico()));
    }

    @Test
    @DisplayName("Execução concluída: só age em OS em execução")
    void deveConcluirExecucao() {
        ordem(OrdemDeServicoStatus.EM_EXECUCAO);
        listener.onExecucaoConcluida(evento());
        verify(concluirExecucao).executar(osId);

        ordem(OrdemDeServicoStatus.AGUARDANDO_PAGAMENTO);
        listener.onExecucaoConcluida(evento());
        verify(concluirExecucao).executar(osId); // continua uma única chamada
    }

    @Test
    @DisplayName("Compensação: falha na execução cancela a OS com o motivo")
    void deveCancelarQuandoExecucaoFalha() {
        ordem(OrdemDeServicoStatus.EM_EXECUCAO);

        listener.onExecucaoFalhou(evento().put("motivo", "Peça indisponível"));

        verify(cancelar).executar(osId, "Falha na execução: Peça indisponível");
    }

    // -------------------------------------------------------------- Billing

    @Test
    @DisplayName("Orçamento aceito: aprova os serviços e avisa os módulos internos")
    void deveAprovarQuandoOrcamentoAceito() {
        UUID aceito = UUID.randomUUID();
        UUID rejeitado = UUID.randomUUID();
        ordem(OrdemDeServicoStatus.AGUARDANDO_APROVACAO);
        JsonObject json = evento()
                .put("servicosAceitos", new JsonArray().add(aceito.toString()))
                .put("servicosRejeitados", new JsonArray().add(rejeitado.toString()));

        listener.onOrcamentoAceito(json);

        verify(aprovar).executar(osId, new AprovarServicoRequest(List.of(aceito), List.of(rejeitado)));
        verify(eventBus).publish(EventosExternos.ORCAMENTO_ACEITO, json);
    }

    @Test
    @DisplayName("Orçamento aceito repetido não reaprova serviços já em execução")
    void deveIgnorarOrcamentoAceitoRepetido() {
        ordem(OrdemDeServicoStatus.EM_EXECUCAO);

        listener.onOrcamentoAceito(evento().put("servicosAceitos", new JsonArray().add(UUID.randomUUID().toString())));

        verifyNoInteractions(aprovar, eventBus);
    }

    @Test
    @DisplayName("Orçamento aceito sem listas de serviços não chama a aprovação")
    void naoDeveAprovarSemServicos() {
        ordem(OrdemDeServicoStatus.AGUARDANDO_APROVACAO);

        listener.onOrcamentoAceito(evento());

        verifyNoInteractions(aprovar);
    }

    @Test
    @DisplayName("Compensação: orçamento rejeitado rejeita a OS")
    void deveRejeitarQuandoOrcamentoRejeitado() {
        ordem(OrdemDeServicoStatus.AGUARDANDO_APROVACAO);
        JsonObject json = evento();

        listener.onOrcamentoRejeitado(json);

        verify(rejeitar).executar(osId);
        verify(eventBus).publish(EventosExternos.ORCAMENTO_REJEITADO, json);
    }

    @Test
    @DisplayName("Orçamento rejeitado de OS já encerrada é ignorado")
    void deveIgnorarRejeicaoDeOsEncerrada() {
        ordem(OrdemDeServicoStatus.REJEITADA);

        listener.onOrcamentoRejeitado(evento());

        verifyNoInteractions(rejeitar, eventBus);
    }

    @Test
    @DisplayName("Compensação: falha ao gerar orçamento cancela a OS")
    void deveCancelarQuandoOrcamentoFalha() {
        ordem(OrdemDeServicoStatus.AGUARDANDO_APROVACAO);

        listener.onOrcamentoFalhou(evento());

        verify(cancelar).executar(osId, "Falha ao gerar o orçamento: não informado");
    }

    @Test
    @DisplayName("Compensação repetida não cancela OS já encerrada")
    void naoDeveCancelarOsEncerrada() {
        ordem(OrdemDeServicoStatus.CANCELADA);

        listener.onOrcamentoFalhou(evento());
        listener.onExecucaoFalhou(evento());

        verify(cancelar, never()).executar(any(), anyString());
    }

    @Test
    @DisplayName("Pagamento confirmado: OS passa para paga e os eventos internos são publicados")
    void deveMarcarComoPaga() {
        OrdemDeServico ordem = ordem(OrdemDeServicoStatus.AGUARDANDO_PAGAMENTO);
        JsonObject json = evento();

        listener.onPagamentoConfirmado(json);

        assertEquals(OrdemDeServicoStatus.PAGA, ordem.getStatus());
        verify(repository).atualizar(ordem);
        verify(eventBus).publish(eq(OrdemServicoPaga.TOPICO), any(JsonObject.class));
        verify(eventBus).publish(EventosExternos.ORCAMENTO_PAGO, json);
    }

    @Test
    @DisplayName("Pagamento confirmado repetido é ignorado")
    void deveIgnorarPagamentoRepetido() {
        OrdemDeServico ordem = ordem(OrdemDeServicoStatus.PAGA);

        listener.onPagamentoConfirmado(evento());

        verify(repository, never()).atualizar(any());
        verifyNoInteractions(eventBus);
        assertEquals(OrdemDeServicoStatus.PAGA, ordem.getStatus());
    }

    @Test
    @DisplayName("Evento de OS desconhecida deve falhar (vai para a DLQ)")
    void deveFalharParaOsDesconhecida() {
        when(repository.buscarPorId(osId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> listener.onExecucaoConcluida(evento()));
    }
}
