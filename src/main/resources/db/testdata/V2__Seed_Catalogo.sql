
-- 1. Inserindo Produtos (50 Peças e Insumos Diversos)
INSERT INTO PRODUTO (id, nome, preco, quantidade_estoque) VALUES 
-- Óleos e Fluidos
('a1b2c3d4-0000-0000-0000-000000000001', 'Óleo de Motor Sintético 5W40 (1 Litro)', 45.90, 100),
('a1b2c3d4-0000-0000-0000-000000000002', 'Óleo de Motor Sintético 5W30 (1 Litro)', 42.50, 120),
('a1b2c3d4-0000-0000-0000-000000000003', 'Óleo de Motor Semissintético 10W40 (1 Litro)', 35.00, 150),
('a1b2c3d4-0000-0000-0000-000000000004', 'Óleo de Motor Mineral 20W50 (1 Litro)', 28.00, 80),
('a1b2c3d4-0000-0000-0000-000000000005', 'Fluido de Freio DOT 4 (500ml)', 30.00, 60),
('a1b2c3d4-0000-0000-0000-000000000006', 'Fluido de Freio DOT 5.1 (500ml)', 45.00, 40),
('a1b2c3d4-0000-0000-0000-000000000007', 'Fluido de Direção Hidráulica (1 Litro)', 38.50, 30),
('a1b2c3d4-0000-0000-0000-000000000008', 'Óleo de Câmbio Manual (1 Litro)', 55.00, 20),
('a1b2c3d4-0000-0000-0000-000000000009', 'Óleo de Câmbio Automático ATF (1 Litro)', 85.00, 50),
('a1b2c3d4-0000-0000-0000-000000000010', 'Aditivo de Radiador Orgânico (1 Litro)', 25.00, 100),
('a1b2c3d4-0000-0000-0000-000000000011', 'Água Desmineralizada (1 Litro)', 6.50, 200),

-- Filtros
('a1b2c3d4-0000-0000-0000-000000000012', 'Filtro de Óleo - Padrão', 25.50, 80),
('a1b2c3d4-0000-0000-0000-000000000013', 'Filtro de Óleo - Premium', 35.00, 40),
('a1b2c3d4-0000-0000-0000-000000000014', 'Filtro de Ar do Motor', 35.00, 60),
('a1b2c3d4-0000-0000-0000-000000000015', 'Filtro de Ar Condicionado (Cabine)', 45.00, 70),
('a1b2c3d4-0000-0000-0000-000000000016', 'Filtro de Combustível', 28.00, 50),

-- Freios
('a1b2c3d4-0000-0000-0000-000000000017', 'Jogo de Pastilhas de Freio Dianteira', 120.00, 30),
('a1b2c3d4-0000-0000-0000-000000000018', 'Jogo de Pastilhas de Freio Traseira', 110.00, 20),
('a1b2c3d4-0000-0000-0000-000000000019', 'Disco de Freio Dianteiro (Unidade)', 180.00, 20),
('a1b2c3d4-0000-0000-0000-000000000020', 'Disco de Freio Traseiro (Unidade)', 160.00, 16),

-- Ignição e Elétrica
('a1b2c3d4-0000-0000-0000-000000000021', 'Jogo de Velas de Ignição (4 unidades)', 140.00, 25),
('a1b2c3d4-0000-0000-0000-000000000022', 'Cabo de Vela (Jogo)', 95.00, 15),
('a1b2c3d4-0000-0000-0000-000000000023', 'Bobina de Ignição', 250.00, 10),
('a1b2c3d4-0000-0000-0000-000000000024', 'Bateria Automotiva 60Ah', 450.00, 12),
('a1b2c3d4-0000-0000-0000-000000000025', 'Lâmpada de Farol H4 55/60W', 25.00, 40),
('a1b2c3d4-0000-0000-0000-000000000026', 'Lâmpada de Farol H7 55W', 28.00, 40),
('a1b2c3d4-0000-0000-0000-000000000027', 'Lâmpada Pingo Lanterna (Par)', 15.00, 50),

-- Suspensão e Direção
('a1b2c3d4-0000-0000-0000-000000000028', 'Amortecedor Dianteiro (Unidade)', 280.00, 16),
('a1b2c3d4-0000-0000-0000-000000000029', 'Amortecedor Traseiro (Unidade)', 240.00, 16),
('a1b2c3d4-0000-0000-0000-000000000030', 'Kit Batente do Amortecedor Dianteiro', 85.00, 20),
('a1b2c3d4-0000-0000-0000-000000000031', 'Bieleta da Suspensão', 65.00, 30),
('a1b2c3d4-0000-0000-0000-000000000032', 'Pivô de Suspensão', 75.00, 25),
('a1b2c3d4-0000-0000-0000-000000000033', 'Terminal de Direção', 80.00, 20),
('a1b2c3d4-0000-0000-0000-000000000034', 'Bucha da Bandeja de Suspensão', 45.00, 40),

-- Motor e Arrefecimento
('a1b2c3d4-0000-0000-0000-000000000035', 'Correia Dentada', 110.00, 20),
('a1b2c3d4-0000-0000-0000-000000000036', 'Tensor da Correia Dentada', 130.00, 15),
('a1b2c3d4-0000-0000-0000-000000000037', 'Correia do Alternador (Poly-V)', 75.00, 25),
('a1b2c3d4-0000-0000-0000-000000000038', 'Bomba D''água', 190.00, 10),
('a1b2c3d4-0000-0000-0000-000000000039', 'Válvula Termostática', 115.00, 15),

-- Embreagem e Transmissão
('a1b2c3d4-0000-0000-0000-000000000040', 'Kit de Embreagem (Platô, Disco e Rolamento)', 650.00, 5),
('a1b2c3d4-0000-0000-0000-000000000041', 'Cabo de Embreagem', 65.00, 15),

-- Sensores e Injeção
('a1b2c3d4-0000-0000-0000-000000000042', 'Bomba de Combustível (Refil)', 145.00, 10),
('a1b2c3d4-0000-0000-0000-000000000043', 'Sensor de Rotação', 180.00, 8),
('a1b2c3d4-0000-0000-0000-000000000044', 'Sensor de Temperatura da Água', 55.00, 15),
('a1b2c3d4-0000-0000-0000-000000000045', 'Sonda Lambda (Sensor de Oxigênio)', 220.00, 8),

-- Insumos de Oficina e Limpeza
('a1b2c3d4-0000-0000-0000-000000000046', 'Palheta do Limpador de Para-brisa (Par)', 55.00, 40),
('a1b2c3d4-0000-0000-0000-000000000047', 'Limpa Contato Elétrico Spray', 22.00, 50),
('a1b2c3d4-0000-0000-0000-000000000048', 'Desengripante Micro Óleo Spray', 18.00, 60),
('a1b2c3d4-0000-0000-0000-000000000049', 'Limpa Para-brisa Concentrado', 12.00, 80),
('a1b2c3d4-0000-0000-0000-000000000050', 'Silicone em Gel (Painel e Borrachas)', 25.00, 30);

-- 2. Inserindo Serviços (Mão de Obra)
INSERT INTO SERVICO (id, descricao, tipo, preco_base) VALUES 
('f9e8d7c6-0000-0000-0000-000000000001', 'Troca de Óleo e Filtros (Básica)', 'MANUTENCAO_PREVENTIVA', 80.00),
('f9e8d7c6-0000-0000-0000-000000000002', 'Revisão do Sistema de Freios Dianteiros', 'MANUTENCAO_CORRETIVA', 150.00),
('f9e8d7c6-0000-0000-0000-000000000003', 'Limpeza e Aditivação do Sistema de Arrefecimento', 'MANUTENCAO_PREVENTIVA', 110.00),
('f9e8d7c6-0000-0000-0000-000000000004', 'Revisão de Ignição (Velas e Cabos)', 'MANUTENCAO_CORRETIVA', 90.00),
('f9e8d7c6-0000-0000-0000-000000000005', 'Substituição de Amortecedores Dianteiros', 'MANUTENCAO_CORRETIVA', 280.00);

INSERT INTO SERVICO_PRODUTO_SUGERIDO (servico_id, produto_id, quantidade) VALUES 
-- Troca de Óleo: 4L de Óleo 5W40 + 1 Filtro Padrão
('f9e8d7c6-0000-0000-0000-000000000001', 'a1b2c3d4-0000-0000-0000-000000000001', 4),
('f9e8d7c6-0000-0000-0000-000000000012', 'a1b2c3d4-0000-0000-0000-000000000012', 1),

-- Revisão de Freios: 1 Jogo de Pastilhas Dianteiras + 1 Fluido DOT 4
('f9e8d7c6-0000-0000-0000-000000000002', 'a1b2c3d4-0000-0000-0000-000000000017', 1),
('f9e8d7c6-0000-0000-0000-000000000002', 'a1b2c3d4-0000-0000-0000-000000000005', 1),

-- Arrefecimento: 2L de Aditivo Orgânico + 3L de Água Desmineralizada
('f9e8d7c6-0000-0000-0000-000000000003', 'a1b2c3d4-0000-0000-0000-000000000010', 2),
('f9e8d7c6-0000-0000-0000-000000000003', 'a1b2c3d4-0000-0000-0000-000000000011', 3),

-- Revisão de Ignição: 1 Jogo de Velas + 1 Jogo de Cabos
('f9e8d7c6-0000-0000-0000-000000000004', 'a1b2c3d4-0000-0000-0000-000000000021', 1),
('f9e8d7c6-0000-0000-0000-000000000004', 'a1b2c3d4-0000-0000-0000-000000000022', 1),

-- Amortecedores: 2 Amortecedores Dianteiros + 2 Kits de Batente
('f9e8d7c6-0000-0000-0000-000000000005', 'a1b2c3d4-0000-0000-0000-000000000028', 2),
('f9e8d7c6-0000-0000-0000-000000000005', 'a1b2c3d4-0000-0000-0000-000000000030', 2);