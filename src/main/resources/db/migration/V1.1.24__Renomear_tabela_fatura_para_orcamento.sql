-- Renomear tabela FATURA para ORCAMENTO para alinhar com a linguagem ubíqua
ALTER TABLE FATURA RENAME TO ORCAMENTO;

-- Renomear constraints se necessário (dependendo do banco de dados)
-- Para PostgreSQL/H2:
ALTER TABLE ORCAMENTO RENAME CONSTRAINT check_fatura_metodo_pagamento TO check_orcamento_metodo_pagamento;
ALTER TABLE ORCAMENTO RENAME CONSTRAINT fk_fatura_os TO fk_orcamento_os;
