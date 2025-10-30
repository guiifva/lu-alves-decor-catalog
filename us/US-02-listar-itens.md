US-02 Listar Itens com Filtros e Paginação

- Papel: como usuário do front do gestor de contratos
- Necessidade: listar itens com filtros por nome, categoria e status
- Valor: localizar rapidamente itens para compor um tema

- Critérios de Aceite
  - GET `/v1/items` retorna 200 com lista paginada.
  - Filtros: `name` (contains, case-insensitive), `category` (igualdade), `active` (boolean).
  - Paginação: `page` (≥0, default 0), `size` (1–100, default 20).
  - Ordenação: `sort` no formato `name,asc|desc` com default `name,asc`.
  - Contrato de resposta inclui `content[]`, `page{number,size,totalElements,totalPages}`, `sort[]`.

- Regras de Negócio/Validações
  - Se `size` > 100, retornar erro 400 com RFC 7807.
  - `name` aplica contains em lower-case; `category` valida enum.

- Camadas e Tarefas
  - Apresentação/API: endpoint GET `/v1/items`; parse de query params; resposta paginada.
  - Aplicação: coordenar filtros e ordenação default.
  - Domínio: sem lógica adicional além das regras de filtro.
  - Infraestrutura: query paginada e ordenada; índices para `lower(name)` e `category`.

