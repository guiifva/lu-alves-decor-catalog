US-05 Atualizar Item Parcialmente (PATCH)

- Papel: como usuário do front do gestor de contratos
- Necessidade: ajustar campos específicos do item (ex.: ativar/desativar)
- Valor: agilidade em pequenas alterações sem substituir o recurso inteiro

- Critérios de Aceite
  - PATCH `/v1/items/{id}` atualiza parcialmente e retorna 200 com o item atualizado.
  - Exemplo: `{ "active": false }` despublica o item.
  - 404 se não encontrado; 422 para validação; 409 para conflito de `sku`.

- Regras de Negócio/Validações
  - Valida apenas campos fornecidos; mantém invariantes gerais.
  - `updated_at` atualizado.

- Camadas e Tarefas
  - Apresentação/API: endpoint PATCH `/v1/items/{id}`; DTO parcial; RFC 7807.
  - Aplicação: mesclar mudanças e validar.
  - Domínio: invariantes do `DecorItem`.
  - Infraestrutura: update parcial seguro.

