package br.com.fiap.oficina.api.modules.atendimento.veiculo.application;

import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.repository.VeiculoRepository;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.usecase.AtivarVeiculoUseCaseImpl;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.usecase.DeletarVeiculoUseCaseImpl;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.usecase.InativarVeiculoUseCaseImpl;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.domain.entity.Veiculo;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Ativar/Inativar/Deletar Veículo - Testes Unitários")
class AtivarInativarVeiculoUseCaseTest {

    private VeiculoRepository repository;
    private AtivarVeiculoUseCaseImpl ativarUseCase;
    private InativarVeiculoUseCaseImpl inativarUseCase;
    private DeletarVeiculoUseCaseImpl deletarUseCase;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(VeiculoRepository.class);
        ativarUseCase = new AtivarVeiculoUseCaseImpl(repository);
        inativarUseCase = new InativarVeiculoUseCaseImpl(repository);
        deletarUseCase = new DeletarVeiculoUseCaseImpl(repository);
    }

    @Test
    @DisplayName("Deve ativar um veículo com sucesso")
    void deveAtivarVeiculo() {
        UUID id = UUID.randomUUID();
        Veiculo veiculo = new Veiculo(UUID.randomUUID(), "ABC-1234", "Fiat", "Palio", 2010);
        veiculo.inativar();

        when(repository.buscarPorId(id)).thenReturn(Optional.of(veiculo));

        ativarUseCase.executar(id);

        verify(repository).atualizar(veiculo);
    }

    @Test
    @DisplayName("Deve inativar um veículo com sucesso")
    void deveInativarVeiculo() {
        UUID id = UUID.randomUUID();
        Veiculo veiculo = new Veiculo(UUID.randomUUID(), "ABC-1234", "Fiat", "Palio", 2010);

        when(repository.buscarPorId(id)).thenReturn(Optional.of(veiculo));

        inativarUseCase.executar(id);

        assertNotNull(veiculo.getDeletadoEm());
        verify(repository).atualizar(veiculo);
    }

    @Test
    @DisplayName("Deve deletar um veículo")
    void deveDeletarVeiculo() {
        UUID id = UUID.randomUUID();
        deletarUseCase.executar(id);
        verify(repository).deletar(id);
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar ativar veículo inexistente")
    void deveLancarErroAoAtivarInexistente() {
        UUID id = UUID.randomUUID();
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> ativarUseCase.executar(id));
    }
}









