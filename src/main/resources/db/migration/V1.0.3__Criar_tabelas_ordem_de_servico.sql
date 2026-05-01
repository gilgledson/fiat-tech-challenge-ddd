-- Tabela principal da Ordem de Serviço
CREATE TABLE ORDEM_DE_SERVICO (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    veiculo_id UUID NOT NULL,
    descricao_problema TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'ABERTA',
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_inicio_execucao TIMESTAMP,
    data_fim_execucao TIMESTAMP,
    deletado_em TIMESTAMP,

    CONSTRAINT fk_os_cliente FOREIGN KEY (cliente_id) REFERENCES CLIENTE(id),
    CONSTRAINT fk_os_veiculo FOREIGN KEY (veiculo_id) REFERENCES VEICULO(id),
    CONSTRAINT chk_os_status CHECK (status IN (
        'ABERTA',
        'ORCAMENTO',
        'AGUARDANDO_APROVACAO',
        'APROVADA',
        'REPROVADA',
        'EM_EXECUCAO',
        'FINALIZADA',
        'CANCELADA'
    ))
);

-- Tabela de Produtos utilizados na Ordem de Serviço
CREATE TABLE ORDEM_DE_SERVICO_PRODUTOS (
    ordem_de_servico_id UUID NOT NULL,
    produto_id UUID NOT NULL,
    nome VARCHAR(255) NOT NULL,
    quantidade INTEGER NOT NULL,
    valor_unitario DECIMAL(10, 2) NOT NULL,
    valor_total DECIMAL(10, 2) NOT NULL,

    CONSTRAINT fk_os_produto_os FOREIGN KEY (ordem_de_servico_id) REFERENCES ORDEM_DE_SERVICO(id),
    CONSTRAINT fk_os_produto_cat FOREIGN KEY (produto_id) REFERENCES PRODUTO(id)
);

-- Tabela de Serviços executados na Ordem de Serviço
CREATE TABLE ORDEM_DE_SERVICO_SERVICOS (
    ordem_de_servico_id UUID NOT NULL,
    servico_id UUID NOT NULL,
    nome VARCHAR(255) NOT NULL,
    quantidade DECIMAL(10, 2) NOT NULL DEFAULT 1,
    valor_unitario DECIMAL(10, 2) NOT NULL,
    valor_total DECIMAL(10, 2) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    data_inicio_execucao TIMESTAMP,
    data_fim_execucao TIMESTAMP,
    CONSTRAINT chk_os_servico_status CHECK (status IN (
        'ABERTA',
        'EM_EXECUCAO',
        'FINALIZADA'
    )),
    CONSTRAINT chk_os_servico_tipo CHECK (tipo IN (
        'PREVENTIVO',
        'CORRETIVO'
    )),

    CONSTRAINT fk_os_servico_os FOREIGN KEY (ordem_de_servico_id) REFERENCES ORDEM_DE_SERVICO(id),
    CONSTRAINT fk_os_servico_cat FOREIGN KEY (servico_id) REFERENCES SERVICO(id)
);

-- Índices para performance em buscas comuns
CREATE INDEX idx_os_cliente_id ON ORDEM_DE_SERVICO(cliente_id);
CREATE INDEX idx_os_veiculo_id ON ORDEM_DE_SERVICO(veiculo_id);
CREATE INDEX idx_os_status ON ORDEM_DE_SERVICO(status);
CREATE INDEX idx_os_produtos_os_id ON ORDEM_DE_SERVICO_PRODUTOS(ordem_de_servico_id);
CREATE INDEX idx_os_servicos_os_id ON ORDEM_DE_SERVICO_SERVICOS(ordem_de_servico_id);
