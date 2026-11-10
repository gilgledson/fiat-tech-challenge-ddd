-- Fase 4 (microsserviços): outbox dos eventos de integração da OS, entregues
-- ao RabbitMQ pelo OutboxRelay.
CREATE TABLE outbox_evento (
    id UUID PRIMARY KEY,
    routing_key VARCHAR(100) NOT NULL,
    payload TEXT NOT NULL,
    correlation_id VARCHAR(100),
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL,
    publicado_em TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_outbox_evento_pendentes ON outbox_evento (criado_em) WHERE publicado_em IS NULL;

-- As tabelas ORCAMENTO e FUNCIONARIOS e as views de relatório continuam no
-- banco por enquanto: os dados passaram a pertencer ao Billing Service e ao
-- Execution Service, mas só devem ser removidos daqui depois de migrados.
