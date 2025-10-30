# Visão Geral

* **Objetivo:** expor, via REST JSON, itens e temas de decoração para reutilização pelo front do gestor de contratos.
* **Escopo v1:**

    * CRUD de **Itens de Decoração**
    * CRUD de **Temas de Decoração**
    * Associação **N:N** entre Tema e Itens
    * Filtros: `name`, `category`, `age_group`
    * Ordenação padrão: `name ASC`
    * Paginação: `page`/`size` (default `size=20`, máx `100`)
    * Status simples: `active` (booleano)
* **Fora do escopo v1:** estoque, autenticação/autorização, SLOs, i18n além de pt-BR, upload/transformação de imagem (usaremos **URLs**).

> Nota: onde existirem escolhas de implementação (ex.: uso de `PUT` vs `PATCH`), padronizei para **PATCH** (parcial) e **PUT** (substituição completa).

---

# Modelo de Domínio

## Entidades

### DecorItem

Campos (baseados no seu JSON Schema):

* `id` (UUID)
* `sku` (string, opcional; único se presente)
* `name` (string, **obrigatório**)
* `category` (enum, **obrigatório**): `Panel | Table | Box_Table | Set | Backdrop | Balloon_Arch | Rug | Fake_Cake | Character_Kit | Cylinder | Vase | Arrangement | LED_Number | Decor_Accessory | Lighting | Furniture | Other`
* `description` (string, opcional)
* `quantity_available` (int ≥ 0, **obrigatório** — **não** usado para gestão de estoque neste v1, mas mantido como metadado)
* `unit` (enum opcional): `unit | set | meter | square_meter | roll | pair | dozen | package | kg | liter | piece | hour | event | other`
* `replacement_value` (number ≥ 0, opcional)
* `rental_price` (number ≥ 0, opcional)
* `photos` (array<uri> min 1, opcional)
* `active` (boolean, default `true`)
* `created_at`/`updated_at` (timestamps)

### Theme

Campos (baseados no seu exemplo):

* `id` (UUID)
* `name` (string, **obrigatório**, único)
* `slug` (string, derivado de `name`, único)
* `description` (string, opcional)
* `images` (array<uri>, opcional)
* `age_group` (string, **obrigatório**; ex.: `Kids`, `Teen`, `Adult`)
* `price_min` (number, **obrigatório** ≥ 0)
* `price_max` (number, **obrigatório** ≥ `price_min`)
* `active` (boolean, default `true`)
* `created_at`/`updated_at` (timestamps)

### ThemeItem (associação N:N)

* `theme_id` (FK Theme)
* `item_id` (FK DecorItem)
* **PK composta** (`theme_id`, `item_id`)
* (Opcional futuro: `note`, `display_name`, `metadata` — vago no v1; **não incluído**)

> Assunção v1: **sem ordenação** dos itens dentro do tema. Se quiser ordem, adicionamos `position` (int) em `ThemeItem` depois.

---

# Regras e Validações

* `name` (Item/Theme): 2–120 chars; **case-insensitive unique** para Theme.
* `slug` de Theme: gerado automaticamente a partir do `name` (kebab-case), e único.
* `price_min` ≤ `price_max`.
* `photos[]`/`images[]`: URIs válidas (https://…).
* `sku`: único se fornecido; regex sugerida: `^[A-Z0-9_-]{3,32}$` (flexível).
* `active`: default `true`; **DELETE** remove o registro (hard delete); para “despublicar”, usar **PATCH** `active=false`.
* Filtros:

    * `name`: busca case-insensitive por `contains`.
    * `category`: igualdade (um por vez no v1).
    * `age_group` (apenas em Theme): igualdade.

---

# API (REST JSON)

## Convenções

* **Base path:** `/v1`
* **Content-Type:** `application/json; charset=utf-8`
* **Erros:** `application/problem+json` (RFC 7807)
* **Paginação:** `GET …?page=0&size=20&sort=name,asc`

    * Resposta com `page.number`, `page.size`, `page.totalElements`, `page.totalPages`, `content[]`.

---

## Itens

### POST `/v1/items`

Cria um item.

**Body (exemplo mínimo):**

```json
{
  "name": "Arco de Balões Pastel",
  "category": "Balloon_Arch",
  "quantity_available": 3,
  "photos": ["https://cdn.example.com/items/balloon-arch-pastel.jpg"]
}
```

**201 Created**

```json
{
  "id": "b0a6f8f8-3e91-4a15-9f69-9f2c7f86b8d1",
  "sku": null,
  "name": "Arco de Balões Pastel",
  "category": "Balloon_Arch",
  "description": null,
  "quantity_available": 3,
  "unit": null,
  "replacement_value": null,
  "rental_price": null,
  "photos": ["https://cdn.example.com/items/balloon-arch-pastel.jpg"],
  "active": true,
  "created_at": "2025-10-30T22:00:00Z",
  "updated_at": "2025-10-30T22:00:00Z"
}
```

**422 UnprocessableEntity** (validação) / **409 Conflict** (sku duplicado) / **400 BadRequest**.

---

### GET `/v1/items`

Lista com filtros, paginação e ordenação.

**Query params:**

* `name` (string, opcional)
* `category` (enum, opcional)
* `active` (boolean, opcional; default: `true`)
* `page` (int≥0, default 0), `size` (1–100, default 20)
* `sort` (ex.: `name,asc`) — **default `name,asc`**

**200 OK**

```json
{
  "content": [ { /* DecorItem */ } ],
  "page": { "number": 0, "size": 20, "totalElements": 57, "totalPages": 3 },
  "sort": ["name,asc"]
}
```

---

### GET `/v1/items/{id}`

**200 OK** → DecorItem
**404 NotFound**

---

### PUT `/v1/items/{id}`

Substitui o recurso (todos os campos não obrigatórios podem ser `null`/omitidos conforme convenção interna).

---

### PATCH `/v1/items/{id}`

Atualização parcial (ex.: ativar/desativar):

```json
{ "active": false }
```

---

### DELETE `/v1/items/{id}`

Remove **definitivamente** o item.
**409 Conflict** se ainda referenciado em temas (opcional; v1 pode permitir deleção que cascata remove vínculos na `ThemeItem`).

---

## Temas

### POST `/v1/themes`

**Body (exemplo mínimo):**

```json
{
  "name": "Jardim Encantado",
  "age_group": "Kids",
  "price_min": 250,
  "price_max": 1200,
  "images": ["https://cdn.example.com/themes/jardim-encantado/1.jpg"]
}
```

**201 Created** → inclui `id`, `slug`, timestamps, `active: true`.

---

### GET `/v1/themes`

Filtros, paginação e ordenação.

**Query params:**

* `name` (string, opcional)
* `age_group` (string, opcional)
* `active` (boolean, opcional; default: `true`)
* `page`, `size`, `sort` (default `name,asc`)

---

### GET `/v1/themes/{id}`

Retorna Theme **com ou sem** itens.

* Para economizar payload, padrão **sem itens**.
* Para incluir itens, usar `?include=items`.

**200 OK** (com `include=items`)

```json
{
  "id": "90bd4d7f-1a0f-4c7d-92a0-b4c89f2d9a0a",
  "name": "Jardim Encantado",
  "slug": "jardim-encantado",
  "description": "Tema com florais pastel...",
  "images": ["https://.../1.jpg"],
  "age_group": "Kids",
  "price_min": 250,
  "price_max": 1200,
  "active": true,
  "items": [
    { "id": "b0a6f8f8-3e91-4a15-9f69-9f2c7f86b8d1", "name": "Arco de Balões Pastel", "category": "Balloon_Arch" }
  ],
  "created_at": "2025-10-30T22:00:00Z",
  "updated_at": "2025-10-30T22:00:00Z"
}
```

---

### PUT `/v1/themes/{id}`

### PATCH `/v1/themes/{id}`

Ex.: despublicar

```json
{ "active": false }
```

### DELETE `/v1/themes/{id}`

Remove o tema e os vínculos em `ThemeItem`.

---

## Vínculos Tema ↔ Itens (N:N)

### POST `/v1/themes/{themeId}/items`

Associa **um ou mais** itens ao tema.

**Body:**

```json
{ "item_ids": ["b0a6f8f8-3e91-4a15-9f69-9f2c7f86b8d1", "f3a3..."] }
```

**200 OK** → retorna lista atualizada de IDs ou o recurso Theme (decida e mantenha padrão; sugiro `{"item_ids":[...]}` leve).

### GET `/v1/themes/{themeId}/items`

Lista itens associados (paginado e ordenável como `/v1/items`, herdando `sort=name,asc`).

### DELETE `/v1/themes/{themeId}/items/{itemId}`

Remove o vínculo específico.

---

# Contratos (Esquemas de Resposta)

## DecorItem (response)

```json
{
  "id": "uuid",
  "sku": "PAN-JARDIM-001",
  "name": "Painel Jardim",
  "category": "Panel",
  "description": "MDF 2x1m, pintura fosca",
  "quantity_available": 5,
  "unit": "unit",
  "replacement_value": 800,
  "rental_price": 150,
  "photos": ["https://.../painel-jardim.jpg"],
  "active": true,
  "created_at": "2025-10-30T22:00:00Z",
  "updated_at": "2025-10-30T22:05:00Z"
}
```

## Theme (response)

```json
{
  "id": "uuid",
  "name": "Enchanted Garden",
  "slug": "enchanted-garden",
  "description": "Whimsical garden theme...",
  "images": ["https://example.com/themes/enchanted-garden/image1.jpg"],
  "age_group": "Kids",
  "price_min": 250,
  "price_max": 1200,
  "active": true,
  "created_at": "2025-10-30T22:00:00Z",
  "updated_at": "2025-10-30T22:05:00Z"
}
```

## Lista Paginada (contrato)

```json
{
  "content": [ /* array de recursos */ ],
  "page": {
    "number": 0,
    "size": 20,
    "totalElements": 42,
    "totalPages": 3
  },
  "sort": ["name,asc"]
}
```

## Erros (RFC 7807)

```json
{
  "type": "https://decor-catalog/api/errors/validation",
  "title": "Validation failed",
  "status": 422,
  "detail": "price_max must be >= price_min",
  "instance": "/v1/themes"
}
```

---

# Persistência (DDL sugerida)

```sql
-- Theme
CREATE TABLE theme (
  id UUID PRIMARY KEY,
  name TEXT NOT NULL,
  slug TEXT NOT NULL UNIQUE,
  description TEXT,
  images JSONB,                -- array de URLs
  age_group TEXT NOT NULL,
  price_min NUMERIC(12,2) NOT NULL CHECK (price_min >= 0),
  price_max NUMERIC(12,2) NOT NULL CHECK (price_max >= price_min),
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE UNIQUE INDEX uq_theme_name_ci ON theme ((lower(name)));

-- DecorItem
CREATE TABLE decor_item (
  id UUID PRIMARY KEY,
  sku TEXT UNIQUE,
  name TEXT NOT NULL,
  category TEXT NOT NULL,
  description TEXT,
  quantity_available INTEGER NOT NULL CHECK (quantity_available >= 0),
  unit TEXT,
  replacement_value NUMERIC(12,2),
  rental_price NUMERIC(12,2),
  photos JSONB,                -- array de URLs
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX ix_item_name_ci ON decor_item ((lower(name)));
CREATE INDEX ix_item_category ON decor_item (category);

-- N:N
CREATE TABLE theme_item (
  theme_id UUID NOT NULL REFERENCES theme(id) ON DELETE CASCADE,
  item_id  UUID NOT NULL REFERENCES decor_item(id) ON DELETE CASCADE,
  PRIMARY KEY (theme_id, item_id)
);
```

> Observação: `images`/`photos` como `JSONB` facilita arrays de URL sem criar tabela auxiliar no v1. Se for necessário metadado por imagem no futuro, migramos para tabela própria.

---

# Integração com o Front do Gestor de Contratos

* **Listagem para escolha de itens**: `GET /v1/items?name=&category=&page=&size=&sort=name,asc`
* **Listagem de temas com itens embutidos** (opcional ao construir tela): `GET /v1/themes?age_group=Kids&sort=name,asc` + `GET /v1/themes/{id}?include=items`
* **Criação/Edição de tema**: `POST/PUT/PATCH /v1/themes`
* **Gerenciamento dos vínculos**: `POST /v1/themes/{id}/items` e `DELETE /v1/themes/{id}/items/{itemId}`

---

# Versionamento & Evolução

* Prefixo de versão `/v1`.
* Mudanças compatíveis: novos campos opcionais, novos filtros.
* Potenciais incrementos v1.x:

    * Ordenação de itens no tema (`position` em `theme_item`)
    * Tagging (Item/Theme)
    * Campos transladáveis
    * Cache HTTP (ETag/If-None-Match)

---

# Observabilidade & Operação (mínimo v1)

* Logs estruturados por rota, `trace_id`.
* Métricas básicas: contagem por endpoint e latência P50/P95.
* Healthcheck: `GET /health` (200 com `status":"UP"`).
* Info: `GET /info` (versão, commit).

---

# Exemplos rápidos de uso

1. **Criar Item**

```http
POST /v1/items
```

```json
{ "name": "Painel Jardim", "category": "Panel", "quantity_available": 2 }
```

2. **Criar Tema**

```http
POST /v1/themes
```

```json
{
  "name": "Enchanted Garden",
  "age_group": "Kids",
  "price_min": 250,
  "price_max": 1200,
  "images": ["https://example.com/themes/enchanted-garden/image1.jpg"]
}
```

3. **Associar Itens ao Tema**

```http
POST /v1/themes/{themeId}/items
```

```json
{ "item_ids": ["<item-uuid-1>", "<item-uuid-2>"] }
```

---

# Checklist de Implementação

* [ ] Endpoints e contratos conforme acima
* [ ] Validações server-side (campos obrigatórios, enums, ranges)
* [ ] Geração automática de `slug` (Theme)
* [ ] Paginação/sort padrão `name,asc`
* [ ] RFC 7807 para erros
* [ ] Migrations (Flyway) com DDL sugerida
* [ ] Seeds opcionais para demo (1–2 temas + 5–10 itens)
* [ ] Health/Info endpoints
* [ ] Logs + métricas básicas

