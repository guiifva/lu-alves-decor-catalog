package nocta.lualvesdecorcatalog.controller.dto.item

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import nocta.lualvesdecorcatalog.core.item.ItemCategory
import nocta.lualvesdecorcatalog.core.item.ItemUnit
import nocta.lualvesdecorcatalog.core.item.PatchItemCommand

private const val SKU_PATTERN = "^[A-Z0-9_-]{3,32}$"

data class PatchItemRequest(
    @Schema(description = "Unique SKU for the item", example = "SKU-123", pattern = SKU_PATTERN)
    @field:Pattern(
        regexp = SKU_PATTERN,
        message = "must match pattern $SKU_PATTERN",
    )
    val sku: String? = null,

    @Schema(description = "Name of the item", example = "Mesa Rústica Patched")
    @field:Size(min = 2, max = 120)
    val name: String? = null,

    @Schema(description = "Category of the item")
    val category: ItemCategory? = null,

    @Schema(description = "Detailed description of the item")
    val description: String? = null,

    @Schema(description = "Quantity available in stock", example = "10")
    @JsonProperty("quantity_available")
    @field:Min(0)
    val quantityAvailable: Int? = null,

    @Schema(description = "Unit of measurement for the item")
    val unit: ItemUnit? = null,

    @Schema(description = "Replacement value in case of loss or damage", example = "150.00")
    @JsonProperty("replacement_value")
    @field:DecimalMin(value = "0.0", inclusive = true)
    val replacementValue: BigDecimal? = null,

    @Schema(description = "Rental price per unit", example = "50.00")
    @JsonProperty("rental_price")
    @field:DecimalMin(value = "0.0", inclusive = true)
    val rentalPrice: BigDecimal? = null,

    @Schema(description = "List of photo URLs for the item")
    val photos: List<String>? = null,

    @Schema(description = "Whether the item is active")
    val active: Boolean? = null,
) {
    fun toCommand() = PatchItemCommand(
        sku = sku,
        name = name,
        category = category,
        description = description,
        quantityAvailable = quantityAvailable,
        unit = unit,
        replacementValue = replacementValue,
        rentalPrice = rentalPrice,
        photos = photos,
        active = active,
    )
}
