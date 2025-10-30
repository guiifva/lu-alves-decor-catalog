US-12 Excluir Tema

- Papel: como usuário do front do gestor de contratos
- Necessidade: remover um tema do catálogo
- Valor: manter apenas temas vigentes

- Critérios de Aceite
  - DELETE `/v1/themes/{id}` remove o tema e vínculos em `ThemeItem` (cascade) e retorna 204.
  - 404 quando não encontrado.

- Regras de Negócio/Validações
  - Hard delete; alternativa de despublicação via PATCH (US-11).

- Camadas e Tarefas
  - Apresentação/API: endpoint DELETE `/v1/themes/{id}`; RFC 7807 para 404.
  - Aplicação: orquestra deleção.
  - Domínio: n/a.
  - Infraestrutura: ON DELETE CASCADE em `theme_item` para `theme_id`.

