package br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase;

import br.com.fiap.oficina.api.modules.catalogo.servico.application.repository.ServicoRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class DeletarServicoUseCaseImpl implements DeletarServicoUseCase {
    private final ServicoRepository servicoRepository;

    @Override
    public void executar(UUID id) {
        servicoRepository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Serviço com o id " + id + " não encontrado"));

        servicoRepository.deletar(id);
    }
}






