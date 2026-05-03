
-- Atualizando a View de Esforço por OS para maior precisão (soma antes do arredondamento)
-- E garantindo que apenas serviços FINALIZADOS sejam contados
CREATE OR REPLACE VIEW vw_relatorio_esforco_os AS
SELECT 
    oss.ordem_de_servico_id,
    COUNT(oss.servico_id) AS total_servicos_realizados,
    ROUND(SUM(EXTRACT(EPOCH FROM (oss.data_fim_execucao - oss.data_inicio_execucao))) / 60) AS esforco_total_minutos
    
FROM ordem_de_servico_servicos oss
WHERE oss.data_inicio_execucao IS NOT NULL 
  AND oss.data_fim_execucao IS NOT NULL
  AND oss.status = 'FINALIZADO'
GROUP BY oss.ordem_de_servico_id;

-- Atualizando a View de Tempo Médio de Serviço com filtro de status FINALIZADO
CREATE OR REPLACE VIEW vw_relatorio_tempo_medio_servico AS
SELECT 
    s.id AS servico_id,
    s.nome AS nome_servico,
    COUNT(*) AS quantidade_execucoes_historicas,
    ROUND(AVG(EXTRACT(EPOCH FROM (oss.data_fim_execucao - oss.data_inicio_execucao)) / 60)) AS tempo_medio_minutos

FROM ordem_de_servico_servicos oss
JOIN servico s ON oss.servico_id = s.id
WHERE oss.data_inicio_execucao IS NOT NULL 
  AND oss.data_fim_execucao IS NOT NULL
  AND oss.status = 'FINALIZADO'
GROUP BY s.id, s.nome;
