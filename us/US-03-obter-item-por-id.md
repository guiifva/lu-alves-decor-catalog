US-03 Obter Item por ID

- Papel: como usuário do front do gestor de contratos
- Necessidade: consultar os detalhes de um item específico
- Valor: visualizar informações completas do item antes de associá-lo a um tema

- Critérios de Aceite
  - GET `/v1/items/{id}` retorna 200 com o `DecorItem` quando existir.
  - Retorna 404 quando não encontrado (RFC 7807).

- Regras de Negócio/Validações
  - `id` deve ser UUID válido.

- Camadas e Tarefas
  - Apresentação/API: endpoint GET `/v1/items/{id}`; mapeamento de 404 para RFC 7807.
  - Aplicação: buscar por ID; nenhuma transformação adicional.
  - Domínio: entidade `DecorItem`.
  - Infraestrutura: repositório com busca por ID.

