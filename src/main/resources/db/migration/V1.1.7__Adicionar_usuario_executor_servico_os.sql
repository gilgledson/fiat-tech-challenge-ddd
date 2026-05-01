ALTER TABLE ORDEM_DE_SERVICO_SERVICOS ADD COLUMN usuario_executor_id UUID;

ALTER TABLE ORDEM_DE_SERVICO_SERVICOS 
ADD CONSTRAINT fk_os_servico_usuario_executor 
FOREIGN KEY (usuario_executor_id) REFERENCES USUARIO(id);

CREATE INDEX idx_os_servicos_usuario_id ON ORDEM_DE_SERVICO_SERVICOS(usuario_executor_id);
