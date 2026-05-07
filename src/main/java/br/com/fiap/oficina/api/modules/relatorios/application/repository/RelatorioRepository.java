package br.com.fiap.oficina.api.modules.relatorios.application.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.fiap.oficina.api.modules.relatorios.domain.RelatorioEsforcoOs;
import br.com.fiap.oficina.api.modules.relatorios.domain.RelatorioTempoMedioServico;

public interface RelatorioRepository {
    Optional<RelatorioEsforcoOs> buscarEsforcoPorOsId(UUID osId);
    List<RelatorioTempoMedioServico> buscarTemposMediosServicos();
}






