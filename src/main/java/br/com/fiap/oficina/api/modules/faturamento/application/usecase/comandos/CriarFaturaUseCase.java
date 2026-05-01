package br.com.fiap.oficina.api.modules.faturamento.application.usecase.comandos;

import br.com.fiap.oficina.api.modules.faturamento.domain.entity.Fatura;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface CriarFaturaUseCase {

    public void execute(Fatura fatura);

}
