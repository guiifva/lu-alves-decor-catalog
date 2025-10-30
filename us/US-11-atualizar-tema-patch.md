US-11 Atualizar Tema Parcialmente (PATCH)

- Papel: como usuário do front do gestor de contratos
- Necessidade: ajustar campos específicos do tema (ex.: `active`)
- Valor: flexibilidade para alterações rápidas

- Critérios de Aceite
  - PATCH `/v1/themes/{id}` aplica mudanças parciais e retorna 200.
  - Exemplo: `{ "active": false }` despublica o tema.
  - 404 se inexistente; 409 quando nome duplicado; 422 para validações.

- Regras de Negócio/Validações
  - Regras de US-07 aplicáveis aos campos informados.
  - Regenerar `slug` quando `name` modificar.

- Camadas e Tarefas
  - Apresentação/API: endpoint PATCH `/v1/themes/{id}`; DTO parcial; RFC 7807.
  - Aplicação: mesclagem e validações; ajuste de `slug`.
  - Domínio: entidade `Theme`.
  - Infraestrutura: update parcial seguro.

