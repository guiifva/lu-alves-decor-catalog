US-15 Remover Vínculo Item↔Tema

- Papel: como usuário do front do gestor de contratos
- Necessidade: desvincular um item específico de um tema
- Valor: ajustar a composição do tema sem excluir o item

- Critérios de Aceite
  - DELETE `/v1/themes/{themeId}/items/{itemId}` remove o vínculo e retorna 204.
  - 404 quando vínculo não existir.

- Regras de Negócio/Validações
  - Operação idempotente aceitável retornar 204 mesmo se já não houver vínculo.

- Camadas e Tarefas
  - Apresentação/API: endpoint DELETE; RFC 7807 para 404 quando aplicável.
  - Aplicação: orquestra remoção.
  - Domínio: n/a.
  - Infraestrutura: deleção em `theme_item` por PK composta.

