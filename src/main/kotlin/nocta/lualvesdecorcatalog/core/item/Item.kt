package nocta.lualvesdecorcatalog.core.item

import java.math.BigDecimal
import java.net.URI
import java.time.OffsetDateTime
import java.util.UUID

private val SKU_REGEX = Regex("^[A-Z0-9_-]{3,32}$")

data class Item(
    val id: UUID? = null,
    val sku: String?,
    val name: String,
    val category: ItemCategory,
    val description: String?,
    val quantityAvailable: Int,
    val unit: ItemUnit?,
    val replacementValue: BigDecimal?,
    val rentalPrice: BigDecimal?,
    val photos: List<String>,
    val active: Boolean,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
) {
    init {
        require(name.length in 2..120) {
            "Item name must have between 2 and 120 characters."
        }
        require(quantityAvailable >= 0) {
            "Item quantity_available must be greater than or equal to 0."
        }
        require(replacementValue == null || replacementValue >= BigDecimal.ZERO) {
            "Item replacement_value must be greater than or equal to 0."
        }
        require(rentalPrice == null || rentalPrice >= BigDecimal.ZERO) {
            "Item rental_price must be greater than or equal to 0."
        }
        if (sku != null) {
            require(SKU_REGEX.matches(sku)) {
                "Item sku must match the pattern ${SKU_REGEX.pattern}."
            }
        }
        photos.forEach { value ->
            require(value.isNotBlank()) {
                "Item photo URLs must not be blank."
            }
            val uri = runCatching { URI(value) }.getOrElse {
                throw IllegalArgumentException("Item photo '$value' is not a valid URI.")
            }
            require(uri.scheme.equals("https", ignoreCase = true)) {
                "Item photo '$value' must use https scheme."
            }
        }
    }

    fun withId(id: UUID) = copy(id = id)

    fun withTimestamps(createdAt: OffsetDateTime, updatedAt: OffsetDateTime) =
        copy(createdAt = createdAt, updatedAt = updatedAt)
}

data class CreateItemCommand(
    val sku: String?,
    val name: String,
    val category: ItemCategory,
    val description: String?,
    val quantityAvailable: Int,
    val unit: ItemUnit?,
    val replacementValue: BigDecimal?,
    val rentalPrice: BigDecimal?,
    val photos: List<String>?,
    val active: Boolean?,
) {
    fun toItem(now: OffsetDateTime): Item = Item(
        id = null,
        sku = sku,
        name = name,
        category = category,
        description = description,
        quantityAvailable = quantityAvailable,
        unit = unit,
        replacementValue = replacementValue,
        rentalPrice = rentalPrice,
        photos = photos?.filter { it.isNotBlank() } ?: emptyList(),
        active = active ?: true,
        createdAt = now,
        updatedAt = now,
    )
}

data class UpdateItemCommand(
    val sku: String?,
    val name: String,
    val category: ItemCategory,
    val description: String?,
    val quantityAvailable: Int,
    val unit: ItemUnit?,
    val replacementValue: BigDecimal?,
    val rentalPrice: BigDecimal?,
    val photos: List<String>,
    val active: Boolean,
)

data class PatchItemCommand(
    val sku: String?,
    val name: String?,
    val category: ItemCategory?,
    val description: String?,
    val quantityAvailable: Int?,
    val unit: ItemUnit?,
    val replacementValue: BigDecimal?,
    val rentalPrice: BigDecimal?,
    val photos: List<String>?,
    val active: Boolean?,
)
