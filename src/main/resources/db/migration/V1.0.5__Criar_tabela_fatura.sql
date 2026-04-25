CREATE TABLE FATURA (
    id UUID PRIMARY KEY,
    ordem_servico_id UUID NOT NULL UNIQUE,
    valor_total DECIMAL(10, 2) NOT NULL,
    data_pagamento TIMESTAMP,
    metodo_pagamento VARCHAR(50),
    status VARCHAR(50) NOT NULL,
    CONSTRAINT fk_fatura_os FOREIGN KEY (ordem_servico_id) REFERENCES ORDEM_SERVICO(id)
);