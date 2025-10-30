US-16 Erros Padronizados (RFC 7807)

- Papel: como consumidor da API
- Necessidade: receber erros padronizados com detalhes úteis
- Valor: facilitar tratamento de erros no front e observabilidade

- Critérios de Aceite
  - Todas as respostas de erro utilizam `application/problem+json` com campos: `type`, `title`, `status`, `detail`, `instance`.
  - 422 para falhas de validação de domínio; 409 para conflitos (ex.: `name`/`sku` duplicados); 404 para não encontrado; 400 para payload/parâmetros inválidos.
  - `instance` contém a rota invocada; `type` aponta para documentação amigável.

- Camadas e Tarefas
  - Apresentação/API: exception handlers globais; mapeamento de validações e conflitos.
  - Aplicação: lançar exceções semânticas adequadas.
  - Domínio: sinalizar violações de invariantes de forma clara.
  - Infraestrutura: traduzir erros de banco (ex.: unique constraint) para 409.

