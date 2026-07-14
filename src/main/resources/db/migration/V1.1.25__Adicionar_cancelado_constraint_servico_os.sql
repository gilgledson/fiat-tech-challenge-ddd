-- OrdemDeServicoServicoStatus.CANCELADO é usado em fluxos reais (RejeitarOrcamentoUseCaseImpl,
-- AprovarOrdemDeServicoUseCaseImpl ao reprovar um serviço), mas a constraint nunca permitiu
-- esse valor, causando falha ao persistir.
ALTER TABLE ORDEM_DE_SERVICO_SERVICOS DROP CONSTRAINT chk_os_servico_status;

ALTER TABLE ORDEM_DE_SERVICO_SERVICOS ADD CONSTRAINT chk_os_servico_status CHECK (status IN (
    'PENDENTE',
    'APROVADO',
    'EM_EXECUCAO',
    'REJEITADO',
    'CANCELADO',
    'FINALIZADO'
));
