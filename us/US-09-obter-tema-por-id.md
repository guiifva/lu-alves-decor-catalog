US-09 Obter Tema por ID (com itens opcional)

- Papel: como usuário do front do gestor de contratos
- Necessidade: consultar os detalhes de um tema, opcionalmente com itens
- Valor: verificar composição do tema antes de ofertar

- Critérios de Aceite
  - GET `/v1/themes/{id}` retorna 200 com `Theme` sem itens por padrão.
  - Quando `?include=items`, incluir `items[]` com `id`, `name`, `category` de cada item.
  - 404 quando não encontrado (RFC 7807).

- Regras de Negócio/Validações
  - `id` UUID válido; `include` aceita valor `items`.

- Camadas e Tarefas
  - Apresentação/API: endpoint GET `/v1/themes/{id}` com suporte a `include=items`.
  - Aplicação: carregar itens apenas quando solicitado; evitar payload desnecessário.
  - Domínio: entidades `Theme` e `DecorItem` (visão reduzida para embed).
  - Infraestrutura: join/consulta dedicada para carregar itens do tema.

