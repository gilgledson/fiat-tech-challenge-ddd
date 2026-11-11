# language: pt
Funcionalidade: Ciclo de vida da ordem de serviço entre os microsserviços
  A ordem de serviço nasce no OS Service, é diagnosticada e executada no
  Execution Service e é orçada e paga no Billing Service. Os serviços só se
  falam por eventos (Saga coreografada), e uma falha no meio do caminho desfaz
  o que os outros já tinham feito.

  Contexto:
    Dado um atendente autenticado
    E um cliente com um veículo cadastrado
    E uma peça com 10 unidades em estoque
    E um serviço preventivo de 120.00 que consome 1 unidade da peça de 65.00

  Cenário: A OS percorre diagnóstico, orçamento, execução e pagamento
    Dado uma ordem de serviço aberta com o serviço
    E que a peça tem 1 unidade reservada
    Quando o mecânico inicia o diagnóstico
    Então a ordem de serviço fica com o status "EM_DIAGNOSTICO"
    Quando o mecânico conclui o diagnóstico
    Então o Billing gera um orçamento de 185.00 para a ordem de serviço
    Quando o cliente aceita o orçamento
    Então a ordem de serviço fica com o status "APROVADA"
    Quando o mecânico executa o serviço e conclui a execução
    Então a ordem de serviço fica com o status "AGUARDANDO_PAGAMENTO"
    E a peça fica com 9 unidades em estoque e 0 reservadas
    Quando o pagamento do orçamento é confirmado
    Então a ordem de serviço fica com o status "PAGA"
    E o orçamento fica com o status "PAGO"

  Cenário: Falha na execução desfaz a reserva de estoque e o orçamento
    Dado uma ordem de serviço aberta com o serviço
    E que a peça tem 1 unidade reservada
    E que o diagnóstico foi concluído e o orçamento foi gerado
    Quando a oficina reporta a falha "Peça indisponível no fornecedor"
    Então a execução fica na fase "FALHOU" com o motivo "Peça indisponível no fornecedor"
    E a ordem de serviço fica com o status "CANCELADA"
    E a peça fica com 10 unidades em estoque e 0 reservadas
    E o orçamento fica com o status "CANCELADA"

  Cenário: Cliente rejeita o orçamento e a OS é encerrada sem execução
    Dado uma ordem de serviço aberta com o serviço
    E que o diagnóstico foi concluído e o orçamento foi gerado
    Quando o cliente rejeita o orçamento
    Então o orçamento fica com o status "CANCELADA"
    E a ordem de serviço fica com o status "REJEITADA"
    E a execução fica na fase "CANCELADA"
    E a peça fica com 10 unidades em estoque e 0 reservadas
