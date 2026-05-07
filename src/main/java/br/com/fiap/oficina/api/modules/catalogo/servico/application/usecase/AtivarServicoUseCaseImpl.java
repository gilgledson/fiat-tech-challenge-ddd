package br.com.fiap.oficina.api.modules.catalogo.servico.application.usecase;

import br.com.fiap.oficina.api.modules.catalogo.servico.application.repository.ServicoRepository;
import br.com.fiap.oficina.api.modules.catalogo.servico.domain.entity.Servico;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class AtivarServicoUseCaseImpl implements AtivarServicoUseCase {
    private final ServicoRepository servicoRepository;

    @Override
    public void executar(UUID id) {
        Servico servico = servicoRepository.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Produto com o id " + id.toString() + " não encontrado"));

        servico.ativar();

        servicoRepository.atualizar(servico);
    }
}






