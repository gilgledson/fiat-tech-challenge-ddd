package br.com.fiap.oficina.api.core.exception;

import java.time.LocalDateTime;

public record ErroResponse(
        int status,
        String erro,
        String mensagem,
        LocalDateTime timestamp
) {}






