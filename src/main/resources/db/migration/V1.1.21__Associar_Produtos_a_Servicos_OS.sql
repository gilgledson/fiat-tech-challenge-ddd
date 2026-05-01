-- Adicionar ID único para itens de serviço na OS e vincular produtos a esses itens
ALTER TABLE ORDEM_DE_SERVICO_SERVICOS ADD COLUMN id UUID DEFAULT gen_random_uuid();
ALTER TABLE ORDEM_DE_SERVICO_SERVICOS ADD PRIMARY KEY (id);

-- Adicionar ID único para itens de produto na OS
ALTER TABLE ORDEM_DE_SERVICO_PRODUTOS ADD COLUMN id UUID DEFAULT gen_random_uuid();
ALTER TABLE ORDEM_DE_SERVICO_PRODUTOS ADD PRIMARY KEY (id);

-- Adicionar coluna de associação no item de produto da OS
ALTER TABLE ORDEM_DE_SERVICO_PRODUTOS ADD COLUMN os_servico_id UUID;

-- Adicionar constraint de chave estrangeira
ALTER TABLE ORDEM_DE_SERVICO_PRODUTOS 
ADD CONSTRAINT fk_os_produto_servico 
FOREIGN KEY (os_servico_id) REFERENCES ORDEM_DE_SERVICO_SERVICOS(id);

-- Index para performance
CREATE INDEX idx_os_produtos_servico_id ON ORDEM_DE_SERVICO_PRODUTOS(os_servico_id);
