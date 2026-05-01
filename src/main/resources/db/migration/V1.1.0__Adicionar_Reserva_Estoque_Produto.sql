-- 1. Adicionamos a coluna de reserva (começando em zero)
ALTER TABLE PRODUTO 
ADD COLUMN quantidade_estoque_reservado DECIMAL(10, 2) NOT NULL DEFAULT 0;

-- 2. Renomeamos a coluna antiga para deixar claro que é o físico
ALTER TABLE PRODUTO 
RENAME COLUMN quantidade_estoque TO quantidade_estoque_fisico;

-- 3. A Regra de Ouro (Check Constraint)
-- O banco NUNCA vai permitir que a reserva seja maior que o estoque físico
ALTER TABLE PRODUTO 
ADD CONSTRAINT chk_produto_reserva_valida 
CHECK (quantidade_estoque_reservado <= quantidade_estoque_fisico);