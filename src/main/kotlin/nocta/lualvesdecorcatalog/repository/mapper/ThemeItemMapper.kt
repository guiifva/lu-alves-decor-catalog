package nocta.lualvesdecorcatalog.repository.mapper

import nocta.lualvesdecorcatalog.core.entity.ThemeItem
import nocta.lualvesdecorcatalog.repository.entity.ThemeItemEntity

fun ThemeItem.toEntity(themeId: Long) = ThemeItemEntity(
    name = name,
    quantity = quantity,
    description = description,
    themeId = themeId
)

fun ThemeItemEntity.toModel() = ThemeItem(
    name = name,
    quantity = quantity,
    description = description
)
