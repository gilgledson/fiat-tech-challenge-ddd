package br.com.fiap.oficina.api.modules.faturamento.application.usecase.consultas;

import java.util.UUID;

public interface BaixarFaturaUseCase {

    public byte[] executar(UUID id);
}
