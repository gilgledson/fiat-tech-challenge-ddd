package br.com.fiap.oficina.api.modules.notificacao.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notificacao {
    private UUID id;
    private UUID clienteId;
    private String titulo;
    private String mensagem;
    private String destinatario;
    private LocalDateTime dataCriacao;
    private boolean enviada;

    public Notificacao() {
        this.id = UUID.randomUUID();
        this.dataCriacao = LocalDateTime.now();
        this.enviada = false;
    }

    public Notificacao(UUID clienteId, String titulo, String mensagem, String destinatario) {
        this();
        this.clienteId = clienteId;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.destinatario = destinatario;
    }
}
