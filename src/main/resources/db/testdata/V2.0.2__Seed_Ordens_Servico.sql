-- SEED EM MASSA DE ORDENS DE SERVIÇO (50 UNIDADES)
-- Distribuídas entre os clientes que possuem veículos para fins de relatório

-- Limpeza prévia para evitar conflitos se rodar novamente (usando cast para UUID)
DELETE FROM ORDEM_DE_SERVICO_PRODUTOS WHERE ordem_de_servico_id::text LIKE 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380d%';
DELETE FROM ORDEM_DE_SERVICO_SERVICOS WHERE ordem_de_servico_id::text LIKE 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380d%';
DELETE FROM ORDEM_DE_SERVICO WHERE id::text LIKE 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380d%';

-- Gerando 50 Ordens de Serviço
DO $$
DECLARE
    os_id UUID;
    cli_id UUID;
    vei_id UUID;
    srv_rec RECORD;
    prod_rec RECORD;
    i INT;
    data_ref TIMESTAMP;
    duracao_min INT;
    total_valid_clients INT;
    total_services INT;
BEGIN
    -- Conta quantos clientes possuem veículos
    SELECT COUNT(DISTINCT cliente_id) INTO total_valid_clients FROM VEICULO;
    
    -- Conta quantos serviços existem no catálogo
    SELECT COUNT(*) INTO total_services FROM SERVICO;

    FOR i IN 1..50 LOOP
        -- Seleciona um cliente e seu veículo de forma circular entre os que possuem veículos
        SELECT v.cliente_id, v.id 
        INTO cli_id, vei_id
        FROM (
            SELECT cliente_id, id, row_number() OVER (ORDER BY id) as rn 
            FROM VEICULO
        ) v
        WHERE v.rn = ((i-1) % total_valid_clients) + 1;

        -- Seleciona um serviço de forma circular do catálogo
        SELECT s.id, s.nome, s.preco_base INTO srv_rec
        FROM (
            SELECT id, nome, preco_base, row_number() OVER (ORDER BY id) as rn 
            FROM SERVICO
        ) s
        WHERE s.rn = ((i-1) % total_services) + 1;

        -- Garante que temos um veículo e um serviço válido antes de prosseguir
        IF cli_id IS NULL OR vei_id IS NULL OR srv_rec.id IS NULL THEN
            CONTINUE;
        END IF;

        os_id := ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380d' || LPAD(i::text, 2, '0'))::UUID;
        data_ref := '2026-01-01 08:00:00'::TIMESTAMP + (i * INTERVAL '1.5 days') + (random() * INTERVAL '4 hours');
        duracao_min := 30 + (random() * 180)::INT; -- Entre 30 e 210 minutos

        -- 1. Inserir OS
        INSERT INTO ORDEM_DE_SERVICO (id, cliente_id, veiculo_id, descricao_problema, status, data_criacao, data_inicio_execucao, data_fim_execucao)
        VALUES (os_id, cli_id, vei_id, 'Manutenção Periódica #' || i, 'FINALIZADA', data_ref, data_ref + INTERVAL '1 hour', data_ref + INTERVAL '1 hour' + (duracao_min * INTERVAL '1 minute'));

        -- 2. Inserir Serviço selecionado
        INSERT INTO ORDEM_DE_SERVICO_SERVICOS (ordem_de_servico_id, servico_id, nome, quantidade, valor_unitario, valor_total, status, data_inicio_execucao, data_fim_execucao)
        VALUES (os_id, srv_rec.id, srv_rec.nome, 1, srv_rec.preco_base, srv_rec.preco_base, 'FINALIZADA', data_ref + INTERVAL '1 hour 5 min', data_ref + INTERVAL '1 hour' + (duracao_min * INTERVAL '1 minute') - INTERVAL '5 min');

        -- 3. Inserir todos os Produtos Sugeridos para este serviço
        FOR prod_rec IN 
            SELECT sp.produto_id, p.nome, sp.quantidade, p.preco_unitario 
            FROM SERVICO_PRODUTO_SUGERIDO sp
            JOIN PRODUTO p ON sp.produto_id = p.id
            WHERE sp.servico_id = srv_rec.id
        LOOP
            INSERT INTO ORDEM_DE_SERVICO_PRODUTOS (ordem_de_servico_id, produto_id, nome, quantidade, valor_unitario, valor_total)
            VALUES (os_id, prod_rec.produto_id, prod_rec.nome, prod_rec.quantidade, prod_rec.preco_unitario, prod_rec.quantidade * prod_rec.preco_unitario);
        END LOOP;

    END LOOP;
END $$;
