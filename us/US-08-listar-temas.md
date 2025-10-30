US-08 Listar Temas com Filtros e Paginação

- Papel: como usuário do front do gestor de contratos
- Necessidade: listar temas filtrando por nome, público e status
- Valor: encontrar rapidamente temas para oferecer ao cliente

- Critérios de Aceite
  - GET `/v1/themes` retorna 200 com lista paginada.
  - Filtros: `name` (contains, ci), `age_group` (igualdade), `active` (boolean).
  - Paginação: `page`/`size` com limites; default `size=20`.
  - Ordenação default `name,asc`; aceita `sort` padrão de query.
  - Resposta inclui metadados de página e `sort`. 

- Regras de Negócio/Validações
  - `size` máximo 100; validação de `age_group` como string.

- Camadas e Tarefas
  - Apresentação/API: endpoint GET `/v1/themes`; parse de filtros; resposta paginada.
  - Aplicação: aplicar ordenação/filtros padrão.
  - Domínio: n/a.
  - Infraestrutura: query paginada/ordenada; índice em `lower(name)`.

