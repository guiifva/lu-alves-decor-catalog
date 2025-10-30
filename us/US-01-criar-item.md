US-01 Criar Item de Decoração

- Papel: como usuário do front do gestor de contratos
- Necessidade: cadastrar um novo item de decoração com dados mínimos
- Valor: disponibilizar itens para composição de temas e orçamentos

- Critérios de Aceite
  - POST `/v1/items` cria um item e retorna 201 com o recurso completo.
  - Campos mínimos: `name`, `category`, `quantity_available`.
  - `active` default true; `created_at`/`updated_at` preenchidos.
  - Valida 422 quando regras de domínio falharem; 409 quando `sku` duplicado; 400 para payload inválido.
  - `photos[]` aceita apenas URIs válidas (https://…).

- Regras de Negócio/Validações
  - `name` 2–120 chars.
  - `category` dentro do enum permitido.
  - `quantity_available` ≥ 0.
  - `sku` único se informado (regex sugerida `^[A-Z0-9_-]{3,32}$`).
  - `replacement_value`/`rental_price` ≥ 0; `unit` dentro do enum.

- Camadas e Tarefas
  - Apresentação/API (controller + dto): endpoint POST `/v1/items`; DTO de request/response; Content-Type `application/json`; erros RFC 7807.
  - Aplicação (service): orquestrar validações, defaults e chamada ao repositório.
  - Domínio (core/entity): entidade `DecorItem` com invariantes e enums.
  - Infraestrutura (repository + mapper): persistência, mapeamento entity↔DTO, índice/constraint de unicidade do `sku`.

