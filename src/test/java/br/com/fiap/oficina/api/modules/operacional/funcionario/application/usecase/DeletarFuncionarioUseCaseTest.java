package br.com.fiap.oficina.api.modules.operacional.funcionario.application.usecase;

import br.com.fiap.oficina.api.modules.operacional.funcionario.application.repository.FuncionarioRepository;
import br.com.fiap.oficina.api.modules.operacional.funcionario.domain.entity.Funcionario;
import br.com.fiap.oficina.api.modules.operacional.funcionario.domain.valueObject.CargoFuncionario;
import jakarta.ws.rs.NotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("Deletar Funcionário - Testes de Uso de Caso")
class DeletarFuncionarioUseCaseTest {

    private FuncionarioRepository repository;
    private DeletarFuncionarioUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(FuncionarioRepository.class);
        useCase = new DeletarFuncionarioUseCaseImpl(repository);
    }

    @Test
    @DisplayName("Deve deletar funcionário permanentemente")
    void deveDeletarPermanente() {
        UUID id = UUID.randomUUID();
        when(repository.buscarPorId(id)).thenReturn(Optional.of(Mockito.mock(Funcionario.class)));

        useCase.executar(id);

        verify(repository, times(1)).deletar(id);
    }

    @Test
    @DisplayName("Deve falhar ao deletar funcionário não encontrado")
    void deveFalharAoDeletarFuncionarioNaoEncontrado() {
        UUID id = UUID.randomUUID();
        when(repository.buscarPorId(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> useCase.executar(id));
    }

    @Test
    @DisplayName("Deve falhar ao deletar funcionário que já está deletado")
    void deveFalharAoDeletarFuncionarioJaDeletado() {
        UUID id = UUID.randomUUID();
        Funcionario funcionario = new Funcionario(UUID.randomUUID(), "João", "Silva", "123.456.789-00",
                "(11) 99999-9999", CargoFuncionario.MECANICO);
        funcionario.desativar();

        when(repository.buscarPorId(id)).thenReturn(Optional.of(funcionario));

        assertThrows(IllegalArgumentException.class, () -> useCase.executar(id));
    }
}
