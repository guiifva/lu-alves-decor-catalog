# Lu Alves Decor Catalog

API reativa para gerenciar temas e itens de decoração da empresa Lu Alves Decor. O serviço permite cadastrar temas completos, visualizar e filtrar o catálogo, atualizar coleções e remover registros, mantendo os dados consolidados em um banco relacional.

## Principais funcionalidades
- Criar temas com sua lista de itens vinculados.
- Listar temas com suporte a paginação e filtro por nome.
- Consultar detalhes de um tema específico.
- Atualizar um tema substituindo a lista de itens associada.
- Remover um tema e seus itens do catálogo.

## Stack
- Kotlin 2.2 com Spring Boot 3.5 (WebFlux e validação reativa).
- Spring Data R2DBC para acesso não bloqueante ao banco de dados.
- Flyway para versionamento de esquema.
- PostgreSQL em produção; H2 em memória para testes.
- Gradle Kotlin DSL como ferramenta de build.
- Springdoc OpenAPI para documentação interativa.

## Pré-requisitos
- JDK 24 (o `gradlew` usa toolchains e baixa uma JDK compatível automaticamente).
- Docker (opcional) caso prefira subir Postgres rapidamente.
- Banco PostgreSQL acessível na porta e credenciais configuradas em `src/main/resources/application.yml`.

## Configuração do banco
Execute um container PostgreSQL com as credenciais padrão do projeto:

```bash
docker run --name decor-db -e POSTGRES_DB=decor_manager -e POSTGRES_USER=user -e POSTGRES_PASSWORD=password -p 5432:5432 -d postgres:16
```

Altere os valores em `application.yml` se quiser personalizar host, porta ou credenciais.

## Como rodar localmente
```bash
./gradlew bootRun
```

- A aplicação sobe em `http://localhost:8080`.
- A documentação interativa fica disponível em `http://localhost:8080/swagger-ui.html` assim que os endpoints estiverem implementados.

## Testes
```bash
./gradlew test
```

Os testes utilizam H2 com R2DBC (`application-test.yml`) e rodam migrações Flyway automaticamente.

## Migrações Flyway
- Coloque os scripts SQL em `src/main/resources/db/migration` no padrão `V1__descricao.sql`.
- No startup, o Flyway aplica as migrações tanto no ambiente principal (PostgreSQL) quanto no de testes (H2).

## Docker
```bash
docker build -t lu-alves-decor-catalog .
docker run --rm -p 8080:8080 \
  -e SPRING_R2DBC_URL=r2dbc:postgresql://host.docker.internal:5432/decor_manager \
  -e SPRING_R2DBC_USERNAME=user \
  -e SPRING_R2DBC_PASSWORD=password \
  -e SPRING_FLYWAY_URL=jdbc:postgresql://host.docker.internal:5432/decor_manager \
  -e SPRING_FLYWAY_USER=user \
  -e SPRING_FLYWAY_PASSWORD=password \
  lu-alves-decor-catalog
```

## Estrutura de pastas
```
.
├── docs/                     # Arquitetura e histórias de usuário
├── src/
│   ├── main/
│   │   ├── kotlin/           # Código fonte da API
│   │   └── resources/        # Configurações e migrações
│   └── test/                 # Testes e configurações de teste
├── build.gradle.kts          # Dependências e plugins Gradle
└── Dockerfile                # Build multi-stage para produção
```

## Documentação adicional
- `docs/architecture.md`: visão geral da arquitetura e camadas planejadas.
- `docs/user-stories/`: histórias de usuário detalhando os cenários de CRUD.

## Próximos passos sugeridos
1. Implementar entidades, repositórios reativos e serviços conforme a arquitetura definida.
2. Criar migrações iniciais Flyway para as tabelas `theme` e `item`.
3. Publicar os endpoints REST e validar via Swagger UI.
