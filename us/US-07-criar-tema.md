US-07 Criar Tema de Decoração

- Papel: como usuário do front do gestor de contratos
- Necessidade: cadastrar um novo tema com faixa de preço e público
- Valor: ofertar conjuntos de itens organizados por tema

- Critérios de Aceite
  - POST `/v1/themes` cria um tema e retorna 201 com `id`, `slug`, timestamps e `active:true`.
  - Campos mínimos: `name`, `age_group`, `price_min`, `price_max`.
  - `slug` gerado automaticamente a partir de `name` (kebab-case), único.
  - `name` único case-insensitive; 409 quando duplicado.
  - 422 quando `price_min` > `price_max` ou demais validações.

- Regras de Negócio/Validações
  - `name` 2–120 chars; unique ci.
  - `age_group` obrigatório (strings como Kids, Teen, Adult).
  - `price_min` ≥ 0; `price_max` ≥ `price_min`.
  - `images[]` são URIs válidas.

- Camadas e Tarefas
  - Apresentação/API: endpoint POST `/v1/themes`; DTOs; RFC 7807.
  - Aplicação: gerar `slug`, validar unicidade de `name` (ci) e preços.
  - Domínio: entidade `Theme` e invariantes.
  - Infraestrutura: persistir `Theme`, índice único em `lower(name)` e `slug`.

