CREATE TABLE FUNCIONARIOS (
    id UUID PRIMARY KEY,
    usuario_id UUID UNIQUE,
    nome VARCHAR(255) NOT NULL,
    sobrenome VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    telefone VARCHAR(15) NOT NULL,
    cargo VARCHAR(50) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT true,
    deletado_em TIMESTAMP,

    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    CONSTRAINT chk_cargo CHECK (cargo IN (
        'MECANICO',
        'ATENDENTE',
        'ADMINISTRADOR'
    ))

);