package nocta.lualvesdecorcatalog.controller.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import nocta.lualvesdecorcatalog.core.entity.DecorItem
import nocta.lualvesdecorcatalog.core.entity.DecorItemCategory
import nocta.lualvesdecorcatalog.core.entity.DecorItemUnit
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class DecorItemResponse(
    val id: UUID,
    val sku: String?,
    val name: String,
    val category: DecorItemCategory,
    val description: String?,
    val quantityAvailable: Int,
    val unit: DecorItemUnit?,
    val replacementValue: BigDecimal?,
    val rentalPrice: BigDecimal?,
    val photos: List<String>,
    val active: Boolean,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
) {
    companion object {
        fun from(item: DecorItem) = DecorItemResponse(
            id = item.id,
            sku = item.sku,
            name = item.name,
            category = item.category,
            description = item.description,
            quantityAvailable = item.quantityAvailable,
            unit = item.unit,
            replacementValue = item.replacementValue,
            rentalPrice = item.rentalPrice,
            photos = item.photos,
            active = item.active,
            createdAt = item.createdAt,
            updatedAt = item.updatedAt
        )
    }
}
