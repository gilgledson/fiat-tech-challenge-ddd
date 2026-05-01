
-- 1. Inserindo Produtos (50 Peças e Insumos Diversos)
INSERT INTO PRODUTO (id, nome, codigo_barras, preco_unitario, quantidade_estoque_fisico, quantidade_estoque_reservado, unidade_medida) VALUES 
-- Óleos e Fluidos
('a1b2c3d4-0000-0000-0000-000000000001', 'Óleo de Motor Sintético 5W40 (1 Litro)', '789123456001', 45.90, 100, 0, 'LT'),
('a1b2c3d4-0000-0000-0000-000000000002', 'Óleo de Motor Sintético 5W30 (1 Litro)', '789123456002', 42.50, 120, 0, 'LT'),
('a1b2c3d4-0000-0000-0000-000000000003', 'Óleo de Motor Semissintético 10W40 (1 Litro)', '789123456003', 35.00, 150, 0, 'LT'),
('a1b2c3d4-0000-0000-0000-000000000004', 'Óleo de Motor Mineral 20W50 (1 Litro)', '789123456004', 28.00, 80, 0, 'LT'),
('a1b2c3d4-0000-0000-0000-000000000005', 'Fluido de Freio DOT 4 (500ml)', '789123456005', 30.00, 60, 0, 'LT'),
('a1b2c3d4-0000-0000-0000-000000000006', 'Fluido de Freio DOT 5.1 (500ml)', '789123456006', 45.00, 40, 0, 'LT'),
('a1b2c3d4-0000-0000-0000-000000000007', 'Fluido de Direção Hidráulica (1 Litro)', '789123456007', 38.50, 30, 0, 'LT'),
('a1b2c3d4-0000-0000-0000-000000000008', 'Óleo de Câmbio Manual (1 Litro)', '789123456008', 55.00, 20, 0, 'LT'),
('a1b2c3d4-0000-0000-0000-000000000009', 'Óleo de Câmbio Automático ATF (1 Litro)', '789123456009', 85.00, 50, 0, 'LT'),
('a1b2c3d4-0000-0000-0000-000000000010', 'Aditivo de Radiador Orgânico (1 Litro)', '789123456010', 25.00, 100, 0, 'LT'),
('a1b2c3d4-0000-0000-0000-000000000011', 'Água Desmineralizada (1 Litro)', '789123456011', 6.50, 200, 0, 'LT'),

-- Filtros
('a1b2c3d4-0000-0000-0000-000000000012', 'Filtro de Óleo - Padrão', '789123456012', 25.50, 80, 0, 'UN'),
('a1b2c3d4-0000-0000-0000-000000000013', 'Filtro de Óleo - Premium', '789123456013', 35.00, 40, 0, 'UN'),
('a1b2c3d4-0000-0000-0000-000000000014', 'Filtro de Ar do Motor', '789123456014', 35.00, 60, 0, 'UN'),
('a1b2c3d4-0000-0000-0000-000000000015', 'Filtro de Ar Condicionado (Cabine)', '789123456015', 45.00, 70, 0, 'UN'),
('a1b2c3d4-0000-0000-0000-000000000016', 'Filtro de Combustível', '789123456016', 28.00, 50, 0, 'UN'),

-- Freios
('a1b2c3d4-0000-0000-0000-000000000017', 'Jogo de Pastilhas de Freio Dianteira', '789123456017', 120.00, 30, 0, 'UN'),
('a1b2c3d4-0000-0000-0000-000000000018', 'Jogo de Pastilhas de Freio Traseira', '789123456018', 110.00, 20, 0, 'UN'),
('a1b2c3d4-0000-0000-0000-000000000019', 'Disco de Freio Dianteiro (Unidade)', '789123456019', 180.00, 20, 0, 'UN'),
('a1b2c3d4-0000-0000-0000-000000000020', 'Disco de Freio Traseiro (Unidade)', '789123456020', 160.00, 16, 0, 'UN'),

-- Ignição e Elétrica
('a1b2c3d4-0000-0000-0000-000000000021', 'Jogo de Velas de Ignição (4 unidades)', '789123456021', 140.00, 25, 0, 'UN'),
('a1b2c3d4-0000-0000-0000-000000000022', 'Cabo de Vela (Jogo)', '789123456022', 95.00, 15, 0, 'UN'),
('a1b2c3d4-0000-0000-0000-000000000023', 'Bobina de Ignição', '789123456023', 250.00, 10, 0, 'UN'),
('a1b2c3d4-0000-0000-0000-000000000024', 'Bateria Automotiva 60Ah', '789123456024', 450.00, 12, 0, 'UN'),
('a1b2c3d4-0000-0000-0000-000000000025', 'Lâmpada de Farol H4 55/60W', '789123456025', 25.00, 40, 0, 'UN'),
('a1b2c3d4-0000-0000-0000-000000000026', 'Lâmpada de Farol H7 55W', '789123456026', 28.00, 40, 0, 'UN'),
('a1b2c3d4-0000-0000-0000-000000000027', 'Lâmpada Pingo Lanterna (Par)', '789123456027', 15.00, 50, 0, 'UN'),

-- Suspensão e Direção
('a1b2c3d4-0000-0000-0000-000000000028', 'Amortecedor Dianteiro (Unidade)', '789123456028', 280.00, 16, 0, 'UN'),
('a1b2c3d4-0000-0000-0000-000000000029', 'Amortecedor Traseiro (Unidade)', '789123456029', 240.00, 16, 0, 'UN'),
('a1b2c3d4-0000-0000-0000-000000000030', 'Kit Batente do Amortecedor Dianteiro', '789123456030', 85.00, 20, 0, 'UN'),
('a1b2c3d4-0000-0000-0000-000000000031', 'Bieleta da Suspensão', '789123456031', 65.00, 30, 0, 'UN'),
('a1b2c3d4-0000-0000-0000-000000000032', 'Pivô de Suspensão', '789123456032', 75.00, 25, 0, 'UN'),
('a1b2c3d4-0000-0000-0000-000000000033', 'Terminal de Direção', '789123456033', 80.00, 20, 0, 'UN'),
('a1b2c3d4-0000-0000-0000-000000000034', 'Bucha da Bandeja de Suspensão', '789123456034', 45.00, 40, 0, 'UN'),

-- Motor e Arrefecimento
('a1b2c3d4-0000-0000-0000-000000000035', 'Correia Dentada', '789123456035', 110.00, 20, 0, 'UN'),
('a1b2c3d4-0000-0000-0000-000000000036', 'Tensor da Correia Dentada', '789123456036', 130.00, 15, 0, 'UN'),
('a1b2c3d4-0000-0000-0000-000000000037', 'Correia do Alternador (Poly-V)', '789123456037', 75.00, 25, 0, 'UN'),
('a1b2c3d4-0000-0000-0000-000000000038', 'Bomba D''água', '789123456038', 190.00, 10, 0, 'UN'),
('a1b2c3d4-0000-0000-0000-000000000039', 'Válvula Termostática', '789123456039', 115.00, 15, 0, 'UN'),

-- Embreagem e Transmissão
('a1b2c3d4-0000-0000-0000-000000000040', 'Kit de Embreagem (Platô, Disco e Rolamento)', '789123456040', 650.00, 5, 0, 'UN'),
('a1b2c3d4-0000-0000-0000-000000000041', 'Cabo de Embreagem', '789123456041', 65.00, 15, 0, 'UN'),

-- Sensores e Injeção
('a1b2c3d4-0000-0000-0000-000000000042', 'Bomba de Combustível (Refil)', '789123456042', 145.00, 10, 0, 'UN'),
('a1b2c3d4-0000-0000-0000-000000000043', 'Sensor de Oxigênio (Sonda Lambda)', '789123456043', 180.00, 12, 0, 'UN'),
('a1b2c3d4-0000-0000-0000-000000000044', 'Sensor de Temperatura do Motor', '789123456044', 65.00, 20, 0, 'UN'),
('a1b2c3d4-0000-0000-0000-000000000045', 'Bico Injetor (Unidade)', '789123456045', 220.00, 16, 0, 'UN'),

-- Insumos e Limpeza
('a1b2c3d4-0000-0000-0000-000000000046', 'Graxa de Lítio (Bisnaga 500g)', '789123456046', 35.00, 30, 0, 'KG'),
('a1b2c3d4-0000-0000-0000-000000000047', 'Limpa Contato Elétrico Spray', '789123456047', 22.00, 50, 0, 'UN'),
('a1b2c3d4-0000-0000-0000-000000000048', 'Desengripante Micro Óleo Spray', '789123456048', 18.00, 60, 0, 'UN'),
('a1b2c3d4-0000-0000-0000-000000000049', 'Limpa Para-brisa Concentrado', '789123456049', 12.00, 80, 0, 'UN'),
('a1b2c3d4-0000-0000-0000-000000000050', 'Silicone em Gel (Painel e Borrachas)', '789123456050', 25.00, 30, 0, 'UN');

-- 2. Inserindo Serviços (Mão de Obra)
INSERT INTO SERVICO (id, nome, tipo, preco_base) VALUES 
('f9e8d7c6-0000-0000-0000-000000000001', 'Troca de Óleo e Filtros (Básica)', 'PREVENTIVO', 80.00),
('f9e8d7c6-0000-0000-0000-000000000002', 'Revisão do Sistema de Freios Dianteiros', 'CORRETIVO', 150.00),
('f9e8d7c6-0000-0000-0000-000000000003', 'Limpeza e Aditivação do Sistema de Arrefecimento', 'PREVENTIVO', 110.00),
('f9e8d7c6-0000-0000-0000-000000000004', 'Revisão de Ignição (Velas e Cabos)', 'CORRETIVO', 90.00),
('f9e8d7c6-0000-0000-0000-000000000005', 'Substituição de Amortecedores Dianteiros', 'CORRETIVO', 280.00);

-- 3. Associações (Produtos Sugeridos para Serviços)
INSERT INTO SERVICO_PRODUTO_SUGERIDO (servico_id, produto_id, quantidade) VALUES 
-- Troca de Óleo (001): 4L de Óleo 5W40 + 1 Filtro Padrão
('f9e8d7c6-0000-0000-0000-000000000001', 'a1b2c3d4-0000-0000-0000-000000000001', 4),
('f9e8d7c6-0000-0000-0000-000000000001', 'a1b2c3d4-0000-0000-0000-000000000012', 1),

-- Revisão de Freios (002): 1 Jogo de Pastilhas Dianteiras + 1 Fluido DOT 4
('f9e8d7c6-0000-0000-0000-000000000002', 'a1b2c3d4-0000-0000-0000-000000000017', 1),
('f9e8d7c6-0000-0000-0000-000000000002', 'a1b2c3d4-0000-0000-0000-000000000005', 1),

-- Arrefecimento (003): 2L Aditivo + 3L Água Desmineralizada
('f9e8d7c6-0000-0000-0000-000000000003', 'a1b2c3d4-0000-0000-0000-000000000010', 2),
('f9e8d7c6-0000-0000-0000-000000000003', 'a1b2c3d4-0000-0000-0000-000000000011', 3),

-- Ignição (004): 1 Jogo de Velas + 1 Jogo de Cabos
('f9e8d7c6-0000-0000-0000-000000000004', 'a1b2c3d4-0000-0000-0000-000000000021', 1),
('f9e8d7c6-0000-0000-0000-000000000004', 'a1b2c3d4-0000-0000-0000-000000000022', 1),

-- Suspensão (005): 2 Amortecedores Dianteiros + 2 Kits Batente
('f9e8d7c6-0000-0000-0000-000000000005', 'a1b2c3d4-0000-0000-0000-000000000028', 2),
('f9e8d7c6-0000-0000-0000-000000000005', 'a1b2c3d4-0000-0000-0000-000000000030', 2);