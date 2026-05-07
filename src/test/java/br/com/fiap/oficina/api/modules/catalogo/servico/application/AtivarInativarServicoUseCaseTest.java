package br.com.fiap.oficina.api.modules.catalogo.servico.application;

import br.com.fiap.oficina.api.modules.catalogo.servico.application.repository.ServicoRepository;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.AtivarServicoUseCaseImpl;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.DeletarServicoUseCaseImpl;
import br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase.InativarServicoUseCaseImpl;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.Servico;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.TipoServico;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Ativar/Inativar/Deletar Serviço - Testes Unitários")
class AtivarInativarServicoUseCaseTest {

    private ServicoRepository repository;
    private AtivarServicoUseCaseImpl ativarUseCase;
    private InativarServicoUseCaseImpl inativarUseCase;
    private DeletarServicoUseCaseImpl deletarUseCase;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(ServicoRepository.class);
        ativarUseCase = new AtivarServicoUseCaseImpl(repository);
        inativarUseCase = new InativarServicoUseCaseImpl(repository);
        deletarUseCase = new DeletarServicoUseCaseImpl(repository);
    }

    @Test
    @DisplayName("Deve ativar um serviço com sucesso")
    void deveAtivarServico() {
        UUID id = UUID.randomUUID();
        Servico servico = new Servico("Troca", TipoServico.PREVENTIVO, BigDecimal.TEN, null);
        servico.inativar();
        
        when(repository.buscarPorId(id)).thenReturn(Optional.of(servico));

        ativarUseCase.executar(id);

        verify(repository).atualizar(servico);
    }

    @Test
    @DisplayName("Deve inativar um serviço com sucesso")
    void deveInativarServico() {
        UUID id = UUID.randomUUID();
        Servico servico = new Servico("Troca", TipoServico.PREVENTIVO, BigDecimal.TEN, null);
        
        when(repository.buscarPorId(id)).thenReturn(Optional.of(servico));

        inativarUseCase.executar(id);

        assertNotNull(servico.getDeletadoEm());
        verify(repository).atualizar(servico);
    }

    @Test
    @DisplayName("Deve deletar um serviço")
    void deveDeletarServico() {
        UUID id = UUID.randomUUID();
        when(repository.buscarPorId(id)).thenReturn(Optional.of(Mockito.mock(Servico.class)));
        deletarUseCase.executar(id);
        verify(repository).deletar(id);
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar ativar serviço inexistente")
    void deveLancarErroAoAtivarInexistente() {
        UUID id = UUID.randomUUID();
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> ativarUseCase.executar(id));
    }
}









