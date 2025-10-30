US-18 Logs Estruturados e Métricas Básicas

- Papel: como operador/observabilidade
- Necessidade: coletar logs e métricas por endpoint
- Valor: identificar problemas e monitorar desempenho (P50/P95)

- Critérios de Aceite
  - Logs estruturados por requisição com `trace_id`, rota, status e duração.
  - Métricas por endpoint com contagem e latências P50/P95.
  - Correlacionar erros (>=400) nos logs com detalhes RFC 7807.

- Camadas e Tarefas
  - Apresentação/API: filtros/interceptadores para logging e timers.
  - Aplicação: propagar `trace_id` no contexto.
  - Domínio: n/a.
  - Infraestrutura: integração com registrador/medição (ex.: Micrometer/SLF4J); exporters conforme ambiente.

