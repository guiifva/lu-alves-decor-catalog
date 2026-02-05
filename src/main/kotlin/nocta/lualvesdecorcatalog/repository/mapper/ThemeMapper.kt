package nocta.lualvesdecorcatalog.repository.mapper

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import nocta.lualvesdecorcatalog.core.entity.Theme
import nocta.lualvesdecorcatalog.core.item.Item
import nocta.lualvesdecorcatalog.repository.entity.ThemeEntity

private val objectMapper = jacksonObjectMapper()

fun ThemeEntity.toModel(items: List<Item>? = null) = Theme(
    id = id,
    slug = slug,
    name = name,
    description = description,
    ageGroup = ageGroup,
    priceMin = priceMin,
    priceMax = priceMax,
    images = images?.let {
        try {
            objectMapper.readValue<List<String>>(it)
        } catch (e: Exception) {
            emptyList()
        }
    } ?: emptyList(),
    active = active,
    createdAt = createdAt,
    updatedAt = updatedAt,
    items = items
)

fun Theme.toEntity() = ThemeEntity(
    id = id,
    slug = slug ?: "", // Should be handled by service before save
    name = name,
    description = description,
    ageGroup = ageGroup,
    priceMin = priceMin,
    priceMax = priceMax,
    images = try {
        objectMapper.writeValueAsString(images)
    } catch (e: Exception) {
        "[]"
    },
    active = active,
    createdAt = createdAt!!,
    updatedAt = updatedAt!!
)
