package nocta.lualvesdecorcatalog.controller.dto.item

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID
import nocta.lualvesdecorcatalog.core.item.Item
import nocta.lualvesdecorcatalog.core.item.ItemCategory
import nocta.lualvesdecorcatalog.core.item.ItemUnit

data class ItemResponse(
    val id: UUID,
    val sku: String?,
    val name: String,
    val category: ItemCategory,
    val description: String?,
    @JsonProperty("quantity_available")
    val quantityAvailable: Int,
    val unit: ItemUnit?,
    @JsonProperty("replacement_value")
    val replacementValue: BigDecimal?,
    @JsonProperty("rental_price")
    val rentalPrice: BigDecimal?,
    val photos: List<String>,
    val active: Boolean,
    @JsonProperty("created_at")
    val createdAt: OffsetDateTime,
    @JsonProperty("updated_at")
    val updatedAt: OffsetDateTime,
) {
    companion object {
        fun from(item: Item) = ItemResponse(
            id = item.id ?: throw IllegalStateException("Item must have an identifier."),
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
            updatedAt = item.updatedAt,
        )
    }
}
