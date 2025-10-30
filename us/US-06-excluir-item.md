US-06 Excluir Item

- Papel: como usuário do front do gestor de contratos
- Necessidade: remover um item que não será mais utilizado
- Valor: manter o catálogo limpo e atualizado

- Critérios de Aceite
  - DELETE `/v1/items/{id}` remove definitivamente o item e retorna 204.
  - Vínculos em `ThemeItem` são removidos por cascade (conforme DDL v1).
  - 404 quando não encontrado.

- Regras de Negócio/Validações
  - Excluir é hard delete; para “despublicar”, usar PATCH `active=false` (US-05).

- Camadas e Tarefas
  - Apresentação/API: endpoint DELETE `/v1/items/{id}`; 204 sem corpo; RFC 7807 para 404.
  - Aplicação: orquestra remoção.
  - Domínio: n/a.
  - Infraestrutura: deleção com ON DELETE CASCADE na tabela de vínculo.

