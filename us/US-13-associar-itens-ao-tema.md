US-13 Associar Itens a um Tema (lote)

- Papel: como usuário do front do gestor de contratos
- Necessidade: vincular um ou mais itens a um tema
- Valor: montar rapidamente a composição de um tema

- Critérios de Aceite
  - POST `/v1/themes/{themeId}/items` recebe `{ "item_ids": [UUID...] }` e retorna 200 com `{ "item_ids": [...] }` atualizado.
  - Itens inexistentes geram 404 (ou 422 com detalhe) sem criar vínculos parciais.
  - Idempotente: vínculos já existentes são ignorados sem erro.

- Regras de Negócio/Validações
  - `themeId` e `item_ids[]` devem ser UUID válidos.

- Camadas e Tarefas
  - Apresentação/API: endpoint POST; validação de payload; RFC 7807.
  - Aplicação: orquestrar upsert de vínculos em lote.
  - Domínio: n/a.
  - Infraestrutura: inserção em `theme_item` com PK composta; transação.

