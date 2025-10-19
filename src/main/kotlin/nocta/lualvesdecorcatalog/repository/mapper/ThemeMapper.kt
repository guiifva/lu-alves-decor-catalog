package nocta.lualvesdecorcatalog.repository.mapper

import nocta.lualvesdecorcatalog.core.entity.Theme
import nocta.lualvesdecorcatalog.repository.entity.ThemeEntity

fun Theme.toEntity() = ThemeEntity(
    name = name,
    description = description
)

fun ThemeEntity.toModel(items: List<nocta.lualvesdecorcatalog.core.entity.Item>) = Theme(
    id = id,
    name = name,
    description = description,
    items = items
)
