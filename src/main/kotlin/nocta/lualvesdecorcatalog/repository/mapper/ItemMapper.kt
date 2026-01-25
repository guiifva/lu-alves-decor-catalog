package nocta.lualvesdecorcatalog.repository.mapper

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import nocta.lualvesdecorcatalog.core.item.Item
import nocta.lualvesdecorcatalog.core.item.ItemCategory
import nocta.lualvesdecorcatalog.core.item.ItemUnit
import nocta.lualvesdecorcatalog.repository.entity.ItemEntity

private val objectMapper = jacksonObjectMapper()
private val photoListType = object : TypeReference<List<String>>() {}

fun ItemEntity.toModel(): Item = Item(
    id = id,
    sku = sku,
    name = name,
    category = ItemCategory.valueOf(category),
    description = description,
    quantityAvailable = quantityAvailable,
    unit = unit?.let { ItemUnit.valueOf(it) },
    replacementValue = replacementValue,
    rentalPrice = rentalPrice,
    photos = photos?.let { objectMapper.readValue(it, photoListType) } ?: emptyList(),
    active = active,
    createdAt = createdAt,
    updatedAt = updatedAt
)

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
    photos = if (photos.isEmpty()) null else objectMapper.writeValueAsString(photos),
    active = active,
    createdAt = createdAt,
    updatedAt = updatedAt
)
