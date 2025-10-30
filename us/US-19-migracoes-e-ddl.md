US-19 Migrações (Flyway) e DDL v1

- Papel: como desenvolvedor/operador
- Necessidade: versionar o schema de banco e aplicá-lo automaticamente
- Valor: reproduzir ambientes e evoluir o domínio com segurança

- Critérios de Aceite
  - Migrations criam tabelas `theme`, `decor_item` e `theme_item` conforme DDL v1 (incluindo índices/constraints sugeridos).
  - Execução automática no start da aplicação; idempotente.
  - `theme_item` com PK composta (`theme_id`,`item_id`) e `ON DELETE CASCADE`.
  - Índices: `uq_theme_name_ci` (lower(name)), índice de `slug` único, índices de busca para item (`lower(name)`, `category`).

- Camadas e Tarefas
  - Apresentação/API: n/a.
  - Aplicação: n/a.
  - Domínio: refletir campos e restrições.
  - Infraestrutura: arquivos Flyway em `resources/db/migration`; validação no boot.

