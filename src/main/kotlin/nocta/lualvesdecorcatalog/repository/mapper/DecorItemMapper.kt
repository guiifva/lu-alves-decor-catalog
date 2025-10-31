package nocta.lualvesdecorcatalog.repository.mapper

import nocta.lualvesdecorcatalog.core.item.Item
import nocta.lualvesdecorcatalog.core.item.ItemCategory
import nocta.lualvesdecorcatalog.core.item.ItemUnit
import nocta.lualvesdecorcatalog.repository.entity.ItemEntity

private const val PHOTO_SEPARATOR = "\n"

fun Item.toEntity(): ItemEntity = ItemEntity(
    id = id,
    sku = sku,
    name = name,
    category = category.name,
    description = description,
    quantityAvailable = quantityAvailable,
    unit = unit?.name,
    replacementValue = replacementValue,
    rentalPrice = rentalPrice,
    photos = photos.takeIf { it.isNotEmpty() }?.joinToString(PHOTO_SEPARATOR),
    active = active,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun ItemEntity.toModel(): Item = Item(
    id = id ?: throw IllegalStateException("Item persisted without identifier."),
    sku = sku,
    name = name,
    category = ItemCategory.valueOf(category),
    description = description,
    quantityAvailable = quantityAvailable,
    unit = unit?.let(ItemUnit::valueOf),
    replacementValue = replacementValue,
    rentalPrice = rentalPrice,
    photos = photos?.split(PHOTO_SEPARATOR)?.filter { it.isNotBlank() } ?: emptyList(),
    active = active,
    createdAt = createdAt,
    updatedAt = updatedAt,
)
