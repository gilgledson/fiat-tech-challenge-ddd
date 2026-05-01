CREATE TABLE IF NOT EXISTS FATURA (
    id UUID PRIMARY KEY,
    ordem_servico_id UUID NOT NULL UNIQUE,
    valor_total DECIMAL(10, 2) NOT NULL,
    data_pagamento TIMESTAMP,
    metodo_pagamento VARCHAR(50),
    data_emissao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_vencimento TIMESTAMP,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT check_fatura_metodo_pagamento CHECK(metodo_pagamento IN ('DINHEIRO', 'CARTAO_CREDITO', 'CARTAO_DEBITO', 'PIX')),
    CONSTRAINT fk_fatura_os FOREIGN KEY (ordem_servico_id) REFERENCES ORDEM_DE_SERVICO(id)
);