package br.com.fiap.oficina.api.modules.relatorios.application.usecase.consultas;

import java.util.UUID;
import java.util.Optional;

import br.com.fiap.oficina.api.modules.relatorios.api.dto.RelatorioEsforcoOsResponse;

public interface ObterRelatorioEsforcoOsUseCase {
    Optional<RelatorioEsforcoOsResponse> executar(UUID osId);
}






