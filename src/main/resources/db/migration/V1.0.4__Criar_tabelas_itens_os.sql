CREATE TABLE ITEM_SERVICO (
    id UUID PRIMARY KEY,
    ordem_servico_id UUID NOT NULL,
    servico_id UUID NOT NULL,
    preco_cobrado DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT fk_item_servico_os FOREIGN KEY (ordem_servico_id) REFERENCES ORDEM_SERVICO(id),
    CONSTRAINT fk_item_servico_cat FOREIGN KEY (servico_id) REFERENCES SERVICO(id),
    CONSTRAINT chk_item_status CHECK (status IN ('PENDENTE','EM_EXECUCAO','CONCLUIDO'))
);

CREATE TABLE ITEM_PRODUTO (
    id UUID PRIMARY KEY,
    ordem_servico_id UUID NOT NULL,
    produto_id UUID NOT NULL,
    quantidade INTEGER NOT NULL,
    preco_cobrado DECIMAL(10, 2) NOT NULL,
    CONSTRAINT fk_item_produto_os FOREIGN KEY (ordem_servico_id) REFERENCES ORDEM_SERVICO(id),
    CONSTRAINT fk_item_produto_cat FOREIGN KEY (produto_id) REFERENCES PRODUTO(id)
);