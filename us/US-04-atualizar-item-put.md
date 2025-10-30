US-04 Atualizar Item (PUT)

- Papel: como usuário do front do gestor de contratos
- Necessidade: substituir completamente os dados de um item
- Valor: manter os dados dos itens consistentes e atualizados

- Critérios de Aceite
  - PUT `/v1/items/{id}` substitui o recurso e retorna 200 com o item atualizado.
  - Campos opcionais podem ser `null`/omitidos conforme convenção; validações de domínio se aplicam.
  - 404 se não encontrado; 422 para validação; 409 para conflito de `sku`.

- Regras de Negócio/Validações
  - Mesmas validações de criação (US-01).
  - `updated_at` atualizado; `created_at` preservado.

- Camadas e Tarefas
  - Apresentação/API: endpoint PUT `/v1/items/{id}`; DTO de entrada/saída; erros RFC 7807.
  - Aplicação: aplicar regras e substituir estado do item.
  - Domínio: invariantes do `DecorItem`.
  - Infraestrutura: update transacional; proteção de unicidade para `sku`.

