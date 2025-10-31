package nocta.lualvesdecorcatalog.repository.mapper

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import nocta.lualvesdecorcatalog.core.entity.DecorItem
import nocta.lualvesdecorcatalog.core.entity.DecorItemCategory
import nocta.lualvesdecorcatalog.core.entity.DecorItemUnit
import nocta.lualvesdecorcatalog.repository.entity.DecorItemEntity

private val objectMapper = jacksonObjectMapper()
private val photoListType = object : TypeReference<List<String>>() {}

fun DecorItemEntity.toDomain(): DecorItem = DecorItem(
    id = id ?: error("Decor item persisted without identifier."),
    sku = sku,
    name = name,
    category = DecorItemCategory.valueOf(category),
    description = description,
    quantityAvailable = quantityAvailable,
    unit = unit?.let { DecorItemUnit.valueOf(it) },
    replacementValue = replacementValue,
    rentalPrice = rentalPrice,
    photos = photos?.let { objectMapper.readValue(it, photoListType) } ?: emptyList(),
    active = active,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun DecorItem.toEntity(): DecorItemEntity = DecorItemEntity(
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
