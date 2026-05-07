package br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.usecase.consulta;

import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.dto.OrdemDeServicoOutput;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.mapper.OrdemDeServicoOutputMapper;
import br.com.fiap.oficina.api.modules.operacional.ordemdeservico.application.repository.OrdemDeServicoRepository;
import br.com.fiap.oficina.api.shared.domain.entity.Pagina;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ListarOrdensDeServicoUseCaseImpl implements ListarOrdensDeServicoUseCase {

        private final OrdemDeServicoRepository repository;

        @Override
        public Pagina<OrdemDeServicoOutput> executar(UUID clienteId, UUID veiculoId, int pagina, int tamanho,
                        boolean incluirInativas) {

                var paginaOrdens = repository.buscarTodas(clienteId, veiculoId, pagina, tamanho, incluirInativas);

                List<OrdemDeServicoOutput> itens = paginaOrdens.itens().stream()
                                .map(OrdemDeServicoOutputMapper::toOutput)
                                .toList();

                return new Pagina<>(itens, paginaOrdens.paginaAtual(), paginaOrdens.tamanhoPagina(),
                                paginaOrdens.totalPaginas(), paginaOrdens.totalElementos());
        }
}






