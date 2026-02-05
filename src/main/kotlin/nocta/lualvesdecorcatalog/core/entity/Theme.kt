package nocta.lualvesdecorcatalog.core.entity

import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID
import nocta.lualvesdecorcatalog.core.item.Item

data class Theme(
    val id: UUID? = null,
    val slug: String? = null,
    val name: String,
    val description: String?,
    val ageGroup: String,
    val priceMin: BigDecimal?,
    val priceMax: BigDecimal?,
    val images: List<String>,
    val active: Boolean,
    val createdAt: OffsetDateTime? = null,
    val updatedAt: OffsetDateTime? = null,
    val items: List<Item>? = null
)

data class CreateThemeCommand(
    val name: String,
    val description: String?,
    val ageGroup: String,
    val priceMin: BigDecimal?,
    val priceMax: BigDecimal?,
    val images: List<String>?,
    val active: Boolean?
)

data class UpdateThemeCommand(
    val name: String,
    val description: String?,
    val ageGroup: String,
    val priceMin: BigDecimal?,
    val priceMax: BigDecimal?,
    val images: List<String>,
    val active: Boolean
)

data class PatchThemeCommand(
    val name: String?,
    val description: String?,
    val ageGroup: String?,
    val priceMin: BigDecimal?,
    val priceMax: BigDecimal?,
    val images: List<String>?,
    val active: Boolean?
)
