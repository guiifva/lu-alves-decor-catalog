US-10 Atualizar Tema (PUT)

- Papel: como usuário do front do gestor de contratos
- Necessidade: substituir completamente os dados de um tema
- Valor: manter informações de tema consistentes

- Critérios de Aceite
  - PUT `/v1/themes/{id}` substitui o recurso e retorna 200.
  - Mantém `slug` consistente com `name` (regenerar se `name` mudar).
  - 404 se inexistente; 409 para nome duplicado (ci); 422 para validações (ex.: `price_min` ≤ `price_max`).

- Regras de Negócio/Validações
  - Mesmas de criação (US-07); `updated_at` atualizado.

- Camadas e Tarefas
  - Apresentação/API: endpoint PUT `/v1/themes/{id}`; DTO; RFC 7807.
  - Aplicação: validações, regeneração de `slug` quando necessário.
  - Domínio: entidade `Theme`.
  - Infraestrutura: update transacional; unicidade de `lower(name)` e `slug`.

