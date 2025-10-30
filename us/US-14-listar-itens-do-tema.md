US-14 Listar Itens de um Tema

- Papel: como usuário do front do gestor de contratos
- Necessidade: visualizar os itens vinculados a um tema
- Valor: revisar e ajustar a composição do tema

- Critérios de Aceite
  - GET `/v1/themes/{themeId}/items` retorna 200 com lista paginada.
  - Herdar paginação e ordenação de `/v1/items` (default `name,asc`).
  - 404 quando `themeId` não existir.

- Regras de Negócio/Validações
  - Suporta filtros relevantes de itens (ex.: `name`, `category`, `active`).

- Camadas e Tarefas
  - Apresentação/API: endpoint GET; parsing de filtros e paginação.
  - Aplicação: orquestrar consulta com filtros.
  - Domínio: n/a.
  - Infraestrutura: join/via tabela `theme_item` com paginação e sort por `name`.

