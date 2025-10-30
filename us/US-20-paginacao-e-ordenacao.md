US-20 Paginação e Ordenação Padrão

- Papel: como consumidor da API
- Necessidade: navegar por grandes coleções com comportamento consistente
- Valor: previsibilidade do front e performance no backend

- Critérios de Aceite
  - Endpoints de listagem (`/v1/items`, `/v1/themes`, `/v1/themes/{id}/items`) aceitam `page` (≥0), `size` (1–100, default 20) e `sort`.
  - Ordenação padrão: `name,asc` quando `sort` ausente.
  - Resposta inclui `content[]`, `page{number,size,totalElements,totalPages}` e `sort[]`.
  - Retornar 400 para parâmetros inválidos.

- Camadas e Tarefas
  - Apresentação/API: padronizar parsing e resposta (objeto de paginação comum).
  - Aplicação: aplicar defaults centralizados.
  - Domínio: n/a.
  - Infraestrutura: consultas paginadas e ordenadas; uso de índices.

