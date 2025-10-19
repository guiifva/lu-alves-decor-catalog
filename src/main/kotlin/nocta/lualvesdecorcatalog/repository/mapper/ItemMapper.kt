package nocta.lualvesdecorcatalog.repository.mapper

import nocta.lualvesdecorcatalog.core.entity.Item
import nocta.lualvesdecorcatalog.repository.entity.ItemEntity

fun Item.toEntity(themeId: Long) = ItemEntity(
    name = name,
    quantity = quantity,
    description = description,
    themeId = themeId
)

fun ItemEntity.toModel() = Item(
    name = name,
    quantity = quantity,
    description = description
)
