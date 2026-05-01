-- Adiciona a coluna de controle de versão
ALTER TABLE PRODUTO 
ADD COLUMN versao BIGINT NOT NULL DEFAULT 0;