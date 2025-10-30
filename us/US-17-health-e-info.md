US-17 Health e Info

- Papel: como operador/observabilidade
- Necessidade: verificar saúde e informações da aplicação
- Valor: garantir operação confiável e diagnósticos rápidos

- Critérios de Aceite
  - GET `/health` retorna 200 e `{ "status": "UP" }` quando saudável.
  - GET `/info` retorna versão, hash do commit e metadados básicos.

- Camadas e Tarefas
  - Apresentação/API: endpoints de health e info.
  - Aplicação: coletar metadados de build.
  - Domínio: n/a.
  - Infraestrutura: integrações leves (ex.: leitura de `build.gradle`/env); readiness/liveness se aplicável.

