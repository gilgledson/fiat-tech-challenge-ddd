CREATE TABLE SERVICO (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    deletado_em TIMESTAMP,
    preco_base DECIMAL(10, 2) NOT NULL,
    CONSTRAINT chk_tipo CHECK (tipo IN ('CORRETIVO','PREVENTIVO'))
);

CREATE TABLE PRODUTO (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    codigo_barras VARCHAR(100) UNIQUE,
    preco_unitario DECIMAL(10, 2) NOT NULL,
    quantidade_estoque DECIMAL(10, 2) NOT NULL DEFAULT 0,
    unidade_medida VARCHAR(100) NOT NULL,
    deletado_em TIMESTAMP,
    CONSTRAINT chk_unidade_medida CHECK (unidade_medida IN ('UN', 'LT', 'KG', 'MT'))
);

CREATE TABLE SERVICO_PRODUTO_SUGERIDO (
    servico_id UUID NOT NULL,
    produto_id UUID NOT NULL,
    quantidade DECIMAL(10, 2) NOT NULL,

    -- Chave estrangeira para o Serviço
    CONSTRAINT fk_servico_associacao
        FOREIGN KEY (servico_id) REFERENCES SERVICO(id),

    -- Chave estrangeira para o Produto (Garante integridade referencial)
    CONSTRAINT fk_produto_associacao
        FOREIGN KEY (produto_id) REFERENCES PRODUTO(id)
);

-- Index para performance em buscas de associação
CREATE INDEX idx_servico_produto_id ON SERVICO_PRODUTO_SUGERIDO(servico_id);